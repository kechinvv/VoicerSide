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

class PerformerNewLine : PerformerWrap("newLine", "\n\n", "")


sealed class PerformerHeader(name: String, level: Int) : PerformerWrap(name, "${"#".repeat(level)} ", "\n")

class PerformerHeader1: PerformerHeader("insertTitle", 1)

class PerformerHeader2: PerformerHeader("insertSubtitle", 2)
