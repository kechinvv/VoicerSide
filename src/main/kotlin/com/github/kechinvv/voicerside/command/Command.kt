package com.github.kechinvv.voicerside.command

interface Command {
    val name: String
    fun perform()
}