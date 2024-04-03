package com.github.kechinvv.voicerside.services

import ai.grazie.utils.capitalize
import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import kotlin.math.max


@Service(Service.Level.PROJECT)
class PluginService {

    private val maxDocumentWidth = 120
    private var lastPartStart = -1

    companion object {
        @JvmStatic
        fun getInstance() = service<PluginService>()
    }

    fun editOpenedFile(editor: Editor, data: String, isText: Boolean = false) {
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

        WriteCommandAction.runWriteCommandAction(editor.project) {
            if (lastPartStart < 0) {
                document.insertString(endOffset, modifiedData)
                lastPartStart = endOffset
            }
            else  document.replaceString(lastPartStart, endOffset, modifiedData)
            lastLine = max(0, document.lineCount - 1)
            editor.caretModel.moveToOffset(document.getLineEndOffset(lastLine))
        }

        if (isText) lastPartStart = -1
    }

    fun runRecognition(editor: Editor) {
        ModelRunner.runRecognition {
            println(it)

            if (it.startsWith("\"text\"")) {
                val content = it.substringAfter("\"text\" : \"")
                    .dropLast(1).capitalize() + ". "

                editOpenedFile(editor, content, true)
            } else if (it.startsWith("\"partial\"")) {
                val content = it.substringAfter("\"partial\" : \"")
                    .dropLast(1)

                editOpenedFile(editor, content)
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
