package com.github.kechinvv.voicerside.actions

import com.github.kechinvv.voicerside.Bundle
import com.github.kechinvv.voicerside.VoicerIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys


class MicAction : AnAction() {
    private var recognition = false

    init {
        this.templatePresentation.disabledIcon = VoicerIcons.MIC_BLOCKED
    }

    override fun update(e: AnActionEvent) {
        if (e.getData(CommonDataKeys.EDITOR) == null || e.getData(CommonDataKeys.VIRTUAL_FILE)?.isDirectory == true) {
            e.presentation.isEnabled = false
            e.presentation.text = Bundle.message("recognition.disabled")
        } else {
            e.presentation.isEnabled = true
            if (recognition) {
                e.presentation.text = Bundle.message("recognition.running")
                e.presentation.icon = VoicerIcons.MIC_RUNNING
            } else {
                e.presentation.text = Bundle.message("recognition.stopped")
                e.presentation.icon = VoicerIcons.MIC_STOPPED
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        recognition = !recognition
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}