package com.github.kechinvv.voicerside.services

import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Job
import java.util.concurrent.Future


@Service(Service.Level.APP)
class PluginService {
    private var docGenerator: Job? = null
    private var future: Future<*>? = null

    companion object {
        @JvmStatic
        fun getInstance() = service<PluginService>()
    }

    fun editOpenedFile(editor: Editor) {
        val document = editor.document
        ApplicationManager.getApplication().runWriteAction {
            document.insertString(0, "aaa ");
        }
    }

    fun runRecognition(project: Project) {

        future = ApplicationManager.getApplication().executeOnPooledThread {
//            ApplicationManager.getApplication().runWriteAction {
            ModelRunner.runRecognition { println(it) }
            //}
        }
    }


    fun stopRecognition() {
//        if (docGenerator?.isActive == true) docGenerator!!.cancel()
        ModelRunner.stop()
    }

    fun isActive(): Boolean {
//        return docGenerator?.isActive == true
        return future?.isDone == false
    }
}
