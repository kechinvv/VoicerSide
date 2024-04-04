package com.github.kechinvv.voicerside.perform.navigation

import com.github.kechinvv.voicerside.Line
import com.github.kechinvv.voicerside.getCurrentLine
import com.intellij.openapi.editor.Editor

sealed class PerformerGotoLine(name: String, private val selector: Line.() -> Int) : PerformerNavigation(name) {

    override fun navigate(editor: Editor) {
        val shift = selector(editor.getCurrentLine()) - editor.caretModel.offset
        editor.moveCaret(columnShift = shift)
    }
}

class PerformerGotoLineStart : PerformerGotoLine("gotoLineStart", { start })
class PerformerGotoLineEnd : PerformerGotoLine("gotoLineEnd", { end })
