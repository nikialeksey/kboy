import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("com.alexeycode.staticanalysis")
}

kotlin {
    applyDefaultHierarchyTemplate()
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
        namespace = "com.alexeycode.kboy.shared"
        minSdk {
            version = release(libs.versions.android.minSdk.get().toInt())
        }
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt())
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KBoyShared"
            isStatic = true
        }
    }
    
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
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kboy-lib"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.io)
                implementation(libs.kotlinx.serialization.json)
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
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.server.websockets)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val nonAndroid by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependencies {
            }
        }
        jvmMain {
            dependsOn(nonAndroid)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.server.cio)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ansi.color)
                implementation(libs.jmdns)
            }
        }
        iosMain {
            dependsOn(nonAndroid)
            dependencies {
            }
        }
        webMain {
            dependsOn(nonAndroid)
        }
    }
}

compose {
    resources {
        packageOfResClass = "com.alexeycode.kboy.shared"
        publicResClass = true
    }
}
