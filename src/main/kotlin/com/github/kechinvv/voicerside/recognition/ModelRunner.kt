package com.github.kechinvv.voicerside.recognition

import org.vosk.Model
import org.vosk.Recognizer
import java.io.ByteArrayOutputStream
import javax.sound.sampled.*


object ModelRunner {

    private val modelPath = ModelRunner::class.java.classLoader.getResource("vosk-model-ru-0.22")!!.path
    private val model = Model(modelPath)

    fun runRecognition(callback: (String) -> Unit) {
        val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            60000f, 16, 2, 4, 44100f, false)
        val info = DataLine.Info(TargetDataLine::class.java, format)
        val microphone = AudioSystem.getLine(info) as TargetDataLine

        Recognizer(model, 120000f).use { recognizer ->
            microphone.open(format)
            microphone.start()
            val out = ByteArrayOutputStream()
            var numBytesRead: Int
            val b = ByteArray(4096)

            callback("START")

            var previousResult: String? = null
            while (true) {
                numBytesRead = microphone.read(b, 0, 1024)
                out.write(b, 0, numBytesRead)
                var result = if (recognizer.acceptWaveForm(b, numBytesRead)) {
                    recognizer.result
                } else {
                    recognizer.partialResult
                }
                result = result.drop(1).dropLast(1).trim()
                if (!result.endsWith(": \"\"") && result != previousResult) {
                    callback(result)
                    previousResult = result
                }
            }
        }
    }
}

fun main() {
    ModelRunner.runRecognition { println(it) }
}