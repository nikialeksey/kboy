import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

kotlin {
    applyDefaultHierarchyTemplate()

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
        webMain {
            dependencies {
                implementation(project(":kboy-shared"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.io)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
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