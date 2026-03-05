import com.android.build.api.dsl.MinSdkVersion
import io.gitlab.arturbosch.detekt.Detekt
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
        namespace = "com.alexeycode.kboy.library"
        minSdk {
            version = release(libs.versions.android.minSdk.get().toInt())
        }
        compileSdk {
            version = release(libs.versions.android.compileSdk.get().toInt())
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "KBoyLib"
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
        outputModuleName = "KBoy"
        browser {
            commonWebpackConfig {
                outputFileName = "KBoy.js"
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
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
        packageOfResClass = "com.alexeycode.kboy.lib"
    }

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