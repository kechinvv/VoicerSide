fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("java") // Java support
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.0.0-SNAPSHOT"
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

// Configure project's dependencies
repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
//    implementation(libs.annotations)
    intellijPlatform {
        intellijIdeaCommunity("2023.3")
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("com.alphacephei:vosk:0.3.45")
}

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(17)
}


intellijPlatform {
    buildSearchableOptions = true
    instrumentCode = false
    projectName = project.name

    pluginConfiguration {
        id = properties("pluginId").get()
        name = properties("pluginName").get()
        version = properties("pluginVersion").get()
        description = ""
        changeNotes = """""".trimIndent()

        ideaVersion {
            sinceBuild = "223"
            untilBuild = "241.*"
        }
    }
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }
}
