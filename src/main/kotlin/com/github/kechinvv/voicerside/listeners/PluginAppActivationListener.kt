package com.github.kechinvv.voicerside.listeners

import com.github.kechinvv.voicerside.services.PluginService
import com.intellij.ide.AppLifecycleListener

internal class PluginAppActivationListener : AppLifecycleListener {

    private val service = PluginService.getInstance()
    override fun appClosing() {
        service.stopRecognition()
    }

    override fun appWillBeClosed(isRestart: Boolean) {
        service.stopRecognition()
    }
}