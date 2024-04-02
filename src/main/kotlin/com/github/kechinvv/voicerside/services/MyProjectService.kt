package com.github.kechinvv.voicerside.services

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project


@Service(Service.Level.PROJECT)
class MyProjectService(private val project: Project) {

    fun editOpenedFile() {
        val document =  FileEditorManager.getInstance(project).selectedTextEditor?.document
        WriteCommandAction.runWriteCommandAction(project) {
            document?.insertString(0, "aaa ");
        }
    }
}
