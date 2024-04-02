package com.github.kechinvv.voicerside.actions

import com.github.kechinvv.voicerside.Bundle
import com.github.kechinvv.voicerside.VoicerIcons
import com.github.kechinvv.voicerside.recognition.ModelRunner
import com.github.kechinvv.voicerside.services.PluginService
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadResult
import com.intellij.openapi.progress.runBackgroundableTask


class MicAction : AnAction() {
    @Volatile
    private var recognition = false

    private val service = PluginService.getInstance()

    init {
        //TODO: disabled icon
        this.templatePresentation.disabledIcon = VoicerIcons.MIC_STOPPED
    }

    override fun update(e: AnActionEvent) {
        if (e.getData(CommonDataKeys.EDITOR) == null || e.getData(CommonDataKeys.VIRTUAL_FILE)?.isDirectory == true) {
            e.presentation.isEnabled = false
            e.presentation.text = Bundle.message("recognition.disabled")
        } else {
            e.presentation.isEnabled = true
            if (service.isActive()) {
                e.presentation.text = Bundle.message("recognition.running")
                e.presentation.icon = VoicerIcons.MIC_RUNNING
            } else {
                e.presentation.text = Bundle.message("recognition.stopped")
                e.presentation.icon = VoicerIcons.MIC_STOPPED
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        if (service.isActive()) service.stopRecognition()
        else if (e.project != null) service.runRecognition(e.project!!)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}