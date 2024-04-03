package com.github.kechinvv.voicerside.perform

import com.intellij.openapi.editor.Editor

sealed interface Performer {
    val name: String
    val isPersistent: Boolean

    fun start(editor: Editor, text: String) {}
    fun perform(editor: Editor, text: String): String
    fun end(editor: Editor, text: String) {}
}