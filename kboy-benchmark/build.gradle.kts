import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.benchmark)
    id("com.alexeycode.kotlinconfiguration")
    id("com.alexeycode.staticanalysis")
}

kotlin {
    applyDefaultHierarchyTemplate()

    iosArm64()
    iosSimulatorArm64()

    jvm()
    
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
