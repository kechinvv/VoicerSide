package com.github.kechinvv.voicerside

import org.vosk.Model
import org.vosk.Recognizer
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

import java.util.*


object ModelRunner {

    private val modelPath = {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        val baseDir =
            if (os.contains("win"))
                Path.of(System.getenv("APPDATA"))
            else
                Path.of(System.getProperty("user.home"))

        baseDir.resolve("model-runner").resolve("model")
    }

    private val model = Model(modelPath.toString())

    fun runRecognition(callback: (String) -> Unit) {
        val format = AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000f, 16, 2, 4, 44100f, false
        )
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
