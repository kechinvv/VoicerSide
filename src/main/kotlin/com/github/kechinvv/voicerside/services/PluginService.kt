package com.github.kechinvv.voicerside.services

import ai.grazie.utils.capitalize
import com.github.kechinvv.voicerside.*
import com.github.kechinvv.voicerside.perform.Performer
import com.github.kechinvv.voicerside.perform.PerformerRegistry
import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream


@OptIn(ExperimentalSerializationApi::class)
@Service(Service.Level.PROJECT)
class PluginService {

    private val maxDocumentWidth = 120
    private var startSentenceOffset = -1

    companion object {
        @JvmStatic
        fun getInstance() = service<PluginService>()
    }

    init {
        PerformerRegistry.registerPerformers()
        this::class.java.classLoader.getResourceAsStream("voice-commands/ru.json")?.use {
            PerformerRegistry.loadVoiceCommands(Json.decodeFromStream(it))
            println("Voice commands are loaded successfully")
        }
    }

    val performers = ArrayDeque<Performer>()

    fun displayMessage(editor: Editor, message: ModelMessage) {
        val content = message.content
        val lineContentLength = editor.getCurrentLine().text.length

        val modifiedContent = when {
            lineContentLength >= maxDocumentWidth -> "\n" + content

            lineContentLength + content.length < maxDocumentWidth -> content

            else -> {
                val index = maxDocumentWidth - lineContentLength
                val beforeIndex = content.substring(0, index)
                val beforeWs = beforeIndex.substringBeforeLast(" ")

                beforeWs + "\n" +
                        content.substring(index + 1 - (beforeIndex.length - beforeWs.length))
            }
        }
        val dot = if (message.type == ModelMessage.Type.TEXT) ". " else ""
        val performedContent = performers.fold(modifiedContent) { d, p -> p.perform(editor, d) } + dot
        editor.write {
            if (startSentenceOffset < 0) {
                startSentenceOffset = caretModel.offset
                document.insertString(caretModel.offset, performedContent)
            } else {
                document.replaceString(startSentenceOffset, caretModel.offset, performedContent)
            }
            caretModel.moveToOffset(caretModel.offset + performedContent.length)
        }
        if (message.type == ModelMessage.Type.TEXT) startSentenceOffset = -1
    }

    fun runRecognition(editor: Editor) {
        // Какой ужас...
        val assistantName = "Док".lowercase()
        var commandMode = false

        ModelRunner.runRecognition {
            println(it)

            ModelMessage.parseOrNull(it)?.let { message ->
                when (message.type) {
                    ModelMessage.Type.PARTIAL -> {
                        if (!commandMode) {
                            if (message.content.startsWith(assistantName)) commandMode = true
                            else displayMessage(editor, message)
                        }  // else: listening to command name to pe parsed in TEXT branch
                    }

                    ModelMessage.Type.TEXT -> {
                        if (commandMode) {
                            val command = message.content.substringAfter(assistantName).trim()
                            PerformerRegistry.getPerformerOrNull(command)?.let { performer ->
                                performer.start(editor)
                                if (performer.isPersistent) performers.addFirst(performer)
                            }
                            commandMode = false
                        } else {
                            val formattedMessage = ModelMessage(
                                ModelMessage.Type.TEXT,
                                message.content.capitalize()
                            )
                            displayMessage(editor, formattedMessage)
                            performers.clear()  // TODO: replace with `stop` command
                        }
                    }
                }
            }
        }
    }


    fun stopRecognition() {
        ModelRunner.stop()
    }

    fun endRecognition() {
        ModelRunner.end()
    }

    fun isActive(): Boolean {
        return ModelRunner.isActive()
    }
}
