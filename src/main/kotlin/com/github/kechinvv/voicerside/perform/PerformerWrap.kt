package com.github.kechinvv.voicerside.perform

import com.intellij.openapi.editor.Editor

sealed class PerformerWrap(
    name: String,
    private val left: String, private val right: String
) : AbstractPerformer(name, true) {

    override fun perform(editor: Editor, text: String): String {
        return "$left$text$right"
    }
}

class PerformerBold : PerformerWrap("makeBold", "**", "**")

class PerformerItalic : PerformerWrap("makeItalic", "*", "*")
