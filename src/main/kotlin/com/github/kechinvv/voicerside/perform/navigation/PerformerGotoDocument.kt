package com.github.kechinvv.voicerside.perform.navigation

import com.github.kechinvv.voicerside.Line
import com.github.kechinvv.voicerside.getCurrentLine
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor

sealed class PerformerGotoDocument(
    name: String,
    private val lineNumberSelector: Document.() -> Int, private val columnShiftSelector: Line.() -> Int
) : PerformerNavigation(name) {

    override fun navigate(editor: Editor) {
        val lineShift = lineNumberSelector(editor.document) - editor.getCurrentLine().number
        editor.moveCaret(lineShift = lineShift)
        // Current line is different at this point
        val columnShift = columnShiftSelector(editor.getCurrentLine()) - editor.caretModel.offset
        editor.moveCaret(columnShift = columnShift)
    }
}

class PerformerGotoDocumentStart : PerformerGotoDocument("gotoDocumentStart", { 0 }, { start })
class PerformerGotoDocumentEnd : PerformerGotoDocument("gotoDocumentEnd", { lineCount - 1 }, { end })