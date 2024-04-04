package com.github.kechinvv.voicerside

data class ModelMessage(
    val type: Type,
    val content: String
) {

    companion object {

        private val separator = Regex(" : ")

        fun parseOrNull(s: String): ModelMessage? {
            val parts = s.split(separator, 2).takeIf { it.size == 2 }?.map { it.trim('"') } ?: return null
            val type = Type.entries.firstOrNull { parts[0].equals(it.name, ignoreCase = true) } ?: return null
            return ModelMessage(type, parts[1])
        }
    }

    enum class Type {
        PARTIAL, TEXT
    }
}