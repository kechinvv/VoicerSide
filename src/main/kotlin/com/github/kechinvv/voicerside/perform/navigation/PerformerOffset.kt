package com.github.kechinvv.voicerside.perform.navigation

import com.intellij.openapi.editor.Editor

sealed class PerformerOffset(name: String, private val lineShift: Int) : PerformerNavigation(name) {

    override fun navigate(editor: Editor) {
        editor.moveCaret(lineShift = lineShift)
    }
}

class PerformerIncrementOffset : PerformerOffset("incrementOffset", +1)
class PerformerDecrementOffset : PerformerOffset("decrementOffset", -1)
