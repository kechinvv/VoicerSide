package com.github.kechinvv.voicerside.perform

import com.github.kechinvv.voicerside.write
import com.intellij.openapi.editor.Editor

sealed class PerformerOffset(name: String, private val lineShift: Int) : AbstractPerformer(name, false) {

    override fun start(editor: Editor) {
        editor.write {
            caretModel.moveCaretRelatively(0, lineShift, false, false, false)
        }
    }
}

class PerformerIncrementOffset : PerformerOffset("incrementOffset", +1)

class PerformerDecrementOffset : PerformerOffset("decrementOffset", -1)
