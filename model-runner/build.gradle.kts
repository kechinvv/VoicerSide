import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    application
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.kechinvv.voicerside"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("com.alphacephei:vosk:0.3.45")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("${project.group}.MainKt")
}

tasks.withType<ShadowJar> {
    isZip64 = true
    archiveFileName.set("model.jar")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
}