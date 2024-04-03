package com.github.kechinvv.voicerside.services

import ai.grazie.utils.capitalize
import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import java.util.concurrent.Future
import kotlin.math.max


@Service(Service.Level.PROJECT)
class PluginService {

    private val maxDocumentWidth = 120

    companion object {
        @JvmStatic
        fun getInstance() = service<PluginService>()
    }

    fun editOpenedFile(editor: Editor, data: String) {
        val document = editor.document

        var lastLine = max(0, document.lineCount - 1)
        val startOffset = document.getLineStartOffset(lastLine)
        val endOffset = document.getLineEndOffset(lastLine)
        val lineContentLength = document.getText(TextRange(startOffset, endOffset)).length

        val modifiedData = when {
            lineContentLength >= maxDocumentWidth -> System.lineSeparator() + data

            lineContentLength + data.length < maxDocumentWidth -> data

            else -> {
                val index = maxDocumentWidth - lineContentLength
                val beforeIndex = data.substring(0, index)
                val beforeWs = beforeIndex.substringBeforeLast(" ")

                beforeWs + System.lineSeparator() +
                        data.substring(index + 1 - (beforeIndex.length - beforeWs.length))
            }
        }

        WriteCommandAction.runWriteCommandAction(editor.project) {
            document.insertString(endOffset, modifiedData)
            lastLine = max(0, document.lineCount - 1)
            editor.caretModel.moveToOffset(document.getLineEndOffset(lastLine))
        }
    }

    fun runRecognition(editor: Editor) {
        ModelRunner.runRecognition {
            println(it)

            if (it.startsWith("\"text\"")) {
                val content = it.substringAfter("\"text\" : \"")
                    .dropLast(1).capitalize() + ". "

                editOpenedFile(editor, content)
            }
        }
    }


    fun stopRecognition() {
        ModelRunner.stop()
    }

    fun isActive(): Boolean {
        return ModelRunner.isActive()
    }
}
