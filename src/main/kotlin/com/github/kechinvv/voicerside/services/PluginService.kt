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

    fun editOpenedFile(editor: Editor, data: String) {
        val lineContentLength = editor.getCurrentLine().text.length

        val modifiedData = when {
            lineContentLength >= maxDocumentWidth -> "\n" + data

            lineContentLength + data.length < maxDocumentWidth -> data

            else -> {
                val index = maxDocumentWidth - lineContentLength
                val beforeIndex = data.substring(0, index)
                val beforeWs = beforeIndex.substringBeforeLast(" ")

                beforeWs + "\n" +
                        data.substring(index + 1 - (beforeIndex.length - beforeWs.length))
            }
        }

        val performedData = performers.fold(modifiedData) { d, p -> p.perform(editor, d) }

        editor.write {
            document.insertString(caretModel.offset, performedData)
            caretModel.moveToOffset(caretModel.offset + performedData.length)
        }
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
                        if (!commandMode && message.content.startsWith(assistantName)) {
                            commandMode = true
                        }
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
                            val formattedSentence = message.content.capitalize() + ". "
                            editOpenedFile(editor, formattedSentence)
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
