import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

kotlin {
    android {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)

            freeCompilerArgs.addAll(
                listOf(
                    "-Xno-param-assertions",
                    "-Xno-receiver-assertions",
                    "-Xno-call-assertions"
                )
            )
        }
    }
    androidLibrary {
        namespace = "com.alexeycode.kboy"
        minSdk = libs.versions.android.minSdk.get().toInt()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KBoy"
            isStatic = true
        }
    }
    
    jvm("desktop") {
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
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "KBoy"
        browser {
            commonWebpackConfig {
                outputFileName = "KBoy.js"
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
        }
        commonMain.dependencies {
            implementation(libs.kotlin.io)
            implementation(libs.kotlin.serialization.json)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.server.cio)
            implementation(libs.ktor.server.websockets)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ansi.color)

            implementation(libs.lwjgl)
            implementation(libs.lwjgl.opengl)
            implementation(libs.lwjgl.jawt)
            implementation(libs.lwjgl3.awt)

            implementation("org.lwjgl:lwjgl:3.3.3:natives-windows")
            implementation("org.lwjgl:lwjgl-opengl:3.3.3:natives-windows")
            implementation("org.lwjgl:lwjgl:3.3.3:natives-linux")
            implementation("org.lwjgl:lwjgl-opengl:3.3.3:natives-linux")

            implementation(libs.jmdns)
        }
        iosMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

compose.desktop {
    application {
        mainClass = "com.alexeycode.kboy.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.alexeycode.kboy"
            packageVersion = "1.0.0"
        }
    }
}
