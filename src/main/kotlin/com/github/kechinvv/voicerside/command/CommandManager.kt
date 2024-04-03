package com.github.kechinvv.voicerside.command

import kotlinx.serialization.json.*
import java.io.File

object CommandManager {

    private val commands = mutableMapOf<String, Command>()
    private lateinit var voiceAliases: Map<String, Set<String>>

    fun loadCommands(jsonConfig: File) {
        voiceAliases = Json.decodeFromString(jsonConfig.readText())
    }

    fun executeCommand(voiceCommand: String): Boolean {
        for ((commandName, aliases) in voiceAliases) {
            if (aliases.contains(voiceCommand)) {
                commands[commandName]?.let { command ->
                    command.perform()
                    return true
                }
            }
        }
        return false
    }

    fun registerCommands() {
        registerCommand(CommandIncrementOffset())
    }

    private fun registerCommand(command: Command) {
        commands[command.name] = command
    }
}