package com.github.kechinvv.voicerside.services

import ai.grazie.utils.capitalize
import com.github.kechinvv.voicerside.ModelMessage
import com.github.kechinvv.voicerside.perform.Performer
import com.github.kechinvv.voicerside.perform.PerformerRegistry
import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlin.math.max


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
        val document = editor.document

        var lastLine = max(0, document.lineCount - 1)
        val startOffset = document.getLineStartOffset(lastLine)
        val endOffset = document.getLineEndOffset(lastLine)
        val lineContentLength = document.getText(TextRange(startOffset, endOffset)).length

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

        WriteCommandAction.runWriteCommandAction(editor.project) {
            document.insertString(endOffset, performedData)
            lastLine = max(0, document.lineCount - 1)
            editor.caretModel.moveToOffset(document.getLineEndOffset(lastLine))
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
                                performers.addFirst(performer)
                                commandMode = false
                            }
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
