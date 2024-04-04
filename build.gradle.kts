fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    id("java") // Java support
    kotlin("jvm") version "1.9.23"
    alias(libs.plugins.gradleIntelliJPlugin) // Gradle IntelliJ Plugin
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
    mavenCentral()
}

intellij {
    pluginName = properties("pluginName")
    version = properties("platformVersion")
    type = properties("platformType")
    plugins = properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
}

// Set the JVM language level used to build the project.
kotlin {
    jvmToolchain(17)
}



tasks {
    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }
}
