package com.github.kechinvv.voicerside.perform.navigation

import com.github.kechinvv.voicerside.perform.AbstractPerformer
import com.github.kechinvv.voicerside.write
import com.intellij.openapi.editor.Editor

sealed class PerformerNavigation(name: String) : AbstractPerformer(name, false) {

    override fun start(editor: Editor) {
        editor.write { navigate(editor) }
    }

    abstract fun navigate(editor: Editor)

    protected fun Editor.moveCaret(
        columnShift: Int = 0,
        lineShift: Int = 0,
        select: Boolean = false,
        scroll: Boolean = true,
    ) {
        caretModel.moveCaretRelatively(columnShift, lineShift, select, false, scroll)
    }
}
