import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xno-param-assertions",
                    "-Xno-receiver-assertions",
                    "-Xno-call-assertions"
                )
            )
            jvmTarget = JvmTarget.JVM_17
        }
    }
    
    sourceSets {
        jvmMain {
            dependencies {
                implementation(project(":kboy-shared"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.io)
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.server.websockets)
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.jmdns)
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "com.alexeycode.kboy.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "com.alexeycode.kboy"
                packageVersion = "1.0.0"
            }
        }
    }
}

detekt {
    autoCorrect = true
    config.setFrom(file("${rootProject.projectDir}/buildLogic/config/detekt/detekt.yml"))
}

tasks.withType<Detekt>().configureEach {
    exclude { it.file.path.contains("build/generated") }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}