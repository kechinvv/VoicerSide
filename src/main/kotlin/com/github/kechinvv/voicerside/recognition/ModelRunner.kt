package com.github.kechinvv.voicerside.recognition

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.*


object ModelRunner {
    private var outputReaderThread: Thread? = null

    @Volatile
    private var stopped = false

    @Volatile
    private var process: Process? = null

    @Volatile
    private var isInitialized = false

    private val modelRunnerPath: Path by lazy {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        val baseDir =
            if (os.contains("win"))
                Path.of(System.getenv("APPDATA"))
            else
                Path.of(System.getProperty("user.home"))

        baseDir.resolve("model-runner").resolve("model.jar")
    }

    fun runRecognition(callback: (String) -> Unit) {
        stopped = false
        if (!isInitialized) {
            val processBuilder = ProcessBuilder("java", "-jar", modelRunnerPath.toString())
            process = processBuilder.start()
            outputReaderThread = Thread {
                val outputReader = BufferedReader(InputStreamReader(process!!.inputStream, "UTF-8"))
                outputReader.lines().iterator()
                    .forEachRemaining { line: String ->
                        if (line == "START")
                            isInitialized = true
                        if (!stopped)
                            callback(line)
                    }
            }

            outputReaderThread!!.start()
        }
    }

    fun stop() {
        stopped = true
    }

    fun end() {
        stop()
        if (process != null) {
            process!!.children().forEach { processHandle: ProcessHandle -> processHandle.destroy() }
            process!!.destroy()
        }
        if (outputReaderThread != null) outputReaderThread!!.interrupt()
    }

    fun isActive(): Boolean {
        return !stopped && isInitialized
    }
}
