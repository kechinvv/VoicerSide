package com.github.kechinvv.voicerside.toolWindow

import com.github.kechinvv.voicerside.VoicerIcons
import com.github.kechinvv.voicerside.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.vladsch.flexmark.parser.core.delimiter.Bracket.image
import java.awt.Component
import java.awt.Graphics
import java.awt.Image
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JButton
import kotlin.math.roundToInt


class VoicerToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val voicerTool = VoicerTool(toolWindow)
        val content = ContentFactory.getInstance().createContent(voicerTool.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class VoicerTool(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent() = JBPanel<JBPanel<*>>().apply {


            val imageButton = JButton("disabled").apply {
                addActionListener {
                    text = if (text == "enabled") "disabled" else "enabled"
                    if (text == "enabled") service.editOpenedFile();
                }
            }
//            imageButton.isBorderPainted = false
//            imageButton.isContentAreaFilled = false
//            imageButton.isFocusPainted = false
//            imageButton.isOpaque = false
            add(imageButton)
//            val label = JBLabel(MyBundle.message("randomLabel", "?"))

//            add(label)
//            add(JButton(MyBundle.message("shuffle")).apply {
//                addActionListener {
//                    label.text = MyBundle.message("randomLabel", service.getRandomNumber())
//                }
//            })
        }


    }
}
