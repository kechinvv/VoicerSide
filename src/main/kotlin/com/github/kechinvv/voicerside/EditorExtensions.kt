package com.github.kechinvv.voicerside

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange

// Sugary sugar

fun Editor.write(block: Editor.() -> Unit) {
    WriteCommandAction.runWriteCommandAction(project) {
        block(this)
    }
}

fun Editor.getCurrentLine(): Line {
    val number = document.getLineNumber(caretModel.offset)
    return Line(document, number)
}

class Line(
    val document: Document,
    val number: Int
) {
    val start: Int
        get() = document.getLineStartOffset(number)

    val end: Int
        get() = document.getLineEndOffset(number)

    val text: String
        get() = document.getText(TextRange(start, end))

    fun delete() {
        document.deleteString(start, end)
    }
}
