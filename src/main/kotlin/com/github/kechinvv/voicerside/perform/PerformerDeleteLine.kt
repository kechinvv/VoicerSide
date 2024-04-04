package com.github.kechinvv.voicerside.perform

import com.github.kechinvv.voicerside.getCurrentLine
import com.github.kechinvv.voicerside.write
import com.intellij.openapi.editor.Editor

class PerformerDeleteLine : AbstractPerformer("deleteLine", false) {

    override fun start(editor: Editor) {
        editor.write {
            getCurrentLine().delete()
        }
    }
}