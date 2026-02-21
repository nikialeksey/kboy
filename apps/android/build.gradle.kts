import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    id("com.alexeycode.buildnumber")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val keystoreDebugPropertiesFile = rootProject.file("keystore/android/keystore-debug.properties")
val keystoreDebugProperties = Properties()
try {
    keystoreDebugProperties.load(FileInputStream(keystoreDebugPropertiesFile))
} catch (ignored: IOException) {}

val keystorePropertiesFile = rootProject.file("keystore/android/keystore.properties")
val keystoreProperties = Properties()
try {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} catch (ignored: IOException) {}

android {
    namespace = "com.alexeycode.kboy"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    signingConfigs {
        create("appDebug") {
            keyAlias = keystoreDebugProperties["keyAlias"] as String
            keyPassword = keystoreDebugProperties["keyPassword"] as String
            storeFile = file(keystoreDebugProperties["storeFile"] as String)
            storePassword = keystoreDebugProperties["storePassword"] as String
        }
        create("appRelease") {
            keyAlias = (keystoreProperties["keyAlias"] as String?) ?: (keystoreDebugProperties["keyAlias"] as String)
            keyPassword = (keystoreProperties["keyPassword"] as String?) ?: (keystoreDebugProperties["keyPassword"] as String)
            storeFile = file((keystoreProperties["storeFile"] as String?) ?: (keystoreDebugProperties["storeFile"] as String))
            storePassword = (keystoreProperties["storePassword"] as String?) ?: (keystoreDebugProperties["storePassword"] as String)
        }
    }

    defaultConfig {
        applicationId = "com.alexeycode.kboy"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionName = "0.0.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs["appDebug"]
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt")
            )
            signingConfig = signingConfigs["appRelease"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.androidx.activity.compose)
    implementation(compose.material3)
    implementation(libs.androidx.core.splashscreen)
}