package com.github.kechinvv.voicerside.perform

object PerformerRegistry {

    private val performers = mutableMapOf<String, Performer>()
    private lateinit var voiceAliases: Map<String, Set<String>>

    fun loadVoiceCommands(map: Map<String, Set<String>>) {
        voiceAliases = map
    }

    fun getPerformerOrNull(voiceCommand: String): Performer? {
        for ((name, aliases) in voiceAliases) {
            if (aliases.contains(voiceCommand)) {
                return performers[name]
            }
        }
        return null
    }

    fun registerPerformers() {
        registerPerformer(PerformerBold())
        registerPerformer(PerformerItalic())
        registerPerformer(PerformerIncrementOffset())
        registerPerformer(PerformerDecrementOffset())
        registerPerformer(PerformerDeleteLine())
    }

    private fun registerPerformer(performer: Performer) {
        performers[performer.name] = performer
    }
}