import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

kotlin {
    androidTarget {
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
        moduleName = "KBoy"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "KBoy.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.kotlin.io)
            implementation(libs.kotlin.serialization.json)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
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
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.alexeycode.kboy"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.alexeycode.kboy"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    debugImplementation(compose.uiTooling)
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
