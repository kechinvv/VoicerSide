package com.github.kechinvv.voicerside.recognition

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.*


object ModelRunner {

    private val modelRunnerPath: Path by lazy {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        val baseDir =
            if (os.contains("win"))
                Path.of(System.getenv("APPDATA"))
            else
                Path.of(System.getProperty("user.home"))

        baseDir.resolve("model-runner").resolve("model.jar")
    }
    private var running = true

    fun runRecognition(callback: (String) -> Unit) {
        running = true
        val processBuilder = ProcessBuilder("java", "-jar", modelRunnerPath.toString())
        val process: Process = processBuilder.start()
        val outputReaderThread = Thread {
            val outputReader = BufferedReader(InputStreamReader(process.inputStream))
            outputReader.lines().iterator()
                .forEachRemaining { line: String ->
                    callback(line)
                }
        }
        outputReaderThread.start()
        process.waitFor()
        outputReaderThread.join()
        process.destroy()
    }

    fun stop() {
        running = false
    }
}
