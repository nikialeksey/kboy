import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_24)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    compileOnly(libs.kotlin.multiplatform.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("buildNumber") {
            id = "com.alexeycode.buildnumber"
            implementationClass = "BuildNumberPlugin"
        }
        register("staticAnalysis") {
            id = "com.alexeycode.staticanalysis"
            implementationClass = "StaticAnalysisPlugin"
        }
        register("kotlinConfiguration") {
            id = "com.alexeycode.kotlinconfiguration"
            implementationClass = "KotlinConfigurationPlugin"
        }
    }
}
