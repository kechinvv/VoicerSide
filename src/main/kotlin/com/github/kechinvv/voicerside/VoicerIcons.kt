package com.github.kechinvv.voicerside

import com.intellij.openapi.util.IconLoader.getIcon
import javax.swing.Icon

object VoicerIcons {
    val MIC_RUNNING: Icon = getIcon("/icons/mic-on-v2.svg", VoicerIcons::class.java)
    val MIC_STOPPED: Icon = getIcon("/icons/mic-off.svg", VoicerIcons::class.java)
    val MIC_BLOCKED: Icon = getIcon("/icons/mic-block-v1.svg", VoicerIcons::class.java)
}