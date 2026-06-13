import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.detekt)
    alias(libs.plugins.benchmark)
}

kotlin {
    applyDefaultHierarchyTemplate()

    iosArm64()
    iosSimulatorArm64()

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
        nodejs()
    }
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kboy-lib"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.benchmark)
            }
        }
    }
}

benchmark {
    targets {
        register("jvm")
        register("wasmJs")
    }

    configurations {
        named("main") {
            warmups = 2
            iterations = 5
            iterationTime = 3
            iterationTimeUnit = "s"
            reportFormat = "text"
        }
    }
}

tasks.register("copyResourcesForWasmJsBenchmark", Copy::class) {
    from("${project.projectDir}/src/commonMain/resources/")
    into("${rootProject.projectDir}/build/wasm/packages/${rootProject.name}-${project.name}-wasmJsBenchmark/src/commonMain/resources")
}

afterEvaluate {
    tasks.named("wasmJsBenchmarkGenerate") {
        dependsOn("copyResourcesForWasmJsBenchmark")
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