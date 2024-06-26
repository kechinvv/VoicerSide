package com.github.kechinvv.voicerside.perform

import com.github.kechinvv.voicerside.services.PluginService
import com.intellij.openapi.editor.Editor

class PerformerShutdown : AbstractPerformer("stop", false) {
    override fun start(editor: Editor) {
        PluginService.getInstance().stopRecognition()
    }
}