group = "dev.luna5ama"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.1.0"
}

apply {
    plugin("kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("it.unimi.dsi:fastutil:8.5.15")
}