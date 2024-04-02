package com.github.kechinvv.voicerside.services

import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import java.util.concurrent.Future


@Service(Service.Level.PROJECT)
class PluginService() {

    // TODO: offset by cursor from editor
    private var offset = 0

    companion object {
        @JvmStatic
        fun getInstance() = service<PluginService>()
    }

    fun editOpenedFile(editor: Editor, data: String) {
        val document = editor.document
        val dataNl = data + "\n"
        WriteCommandAction.runWriteCommandAction(editor.project) {
            document.insertString(offset, data);
        }
        offset += dataNl.length
    }

    fun runRecognition(editor: Editor) {
        ModelRunner.runRecognition {
            println(it)
            editOpenedFile(editor, it)
        }
    }


    fun stopRecognition() {
        ModelRunner.stop()
    }

    fun isActive(): Boolean {
        return ModelRunner.isActive()
    }
}
