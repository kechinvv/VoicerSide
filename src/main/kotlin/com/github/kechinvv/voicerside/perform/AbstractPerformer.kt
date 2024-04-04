package com.github.kechinvv.voicerside.perform

import com.intellij.openapi.editor.Editor

abstract class AbstractPerformer(
    override val name: String,
    override val isPersistent: Boolean
) : Performer {

    override fun start(editor: Editor) {}

    override fun perform(editor: Editor, text: String): String {
        return text
    }

    override fun end(editor: Editor) {}
}