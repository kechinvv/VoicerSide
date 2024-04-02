package com.github.kechinvv.voicerside.recognition

import com.intellij.openapi.application.ApplicationManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.*


object ModelRunner {
    private lateinit var outputReaderThread: Runnable

    @Volatile
    private var stopped = false

    @Volatile
    private var process: Process? = null

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
        val processBuilder = ProcessBuilder("java", "-jar", modelRunnerPath.toString())
        process = processBuilder.start()
        outputReaderThread = Runnable {
        val outputReader = BufferedReader(InputStreamReader(process!!.inputStream))
        outputReader.lines().iterator()
            .forEachRemaining { line: String ->
                if (stopped) return@forEachRemaining
                callback(line)
            }
        }
        ApplicationManager.getApplication()
            .invokeLater(outputReaderThread)
//        process!!.waitFor()
//        process!!.destroy()
    }

    fun stop() {
        stopped = true
        process!!.children().forEach { processHandle: ProcessHandle -> processHandle.destroy() }
        process!!.destroy()
    }

    fun isActive(): Boolean {
        return if (process != null) process!!.isAlive
        else false
    }
}
