import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

private val JVM_TARGET = JvmTarget.JVM_25
private val JAVA_VERSION = JavaVersion.VERSION_25
private val KOTLIN_VERSION = KotlinVersion.KOTLIN_2_4

class KotlinConfigurationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val kmpExtension = target.extensions.findByType(KotlinMultiplatformExtension::class.java)
        if (kmpExtension != null) {
            kmpExtension.compilerOptions {
                languageVersion.set(KOTLIN_VERSION)
            }
            target.afterEvaluate {
                kmpExtension.targets.onEach { target ->
                    if (target is KotlinJvmTarget) {
                        target.compilerOptions {
                            jvmTarget.set(JVM_TARGET)
                        }
                    } else if (target is KotlinMultiplatformAndroidLibraryTarget) {
                        target.compilerOptions {
                            jvmTarget.set(JVM_TARGET)
                        }
                    }
                }
            }
        }

        val androidExtension = target.extensions.findByType(CommonExtension::class.java)
        if (androidExtension != null) {
            androidExtension.compileOptions.sourceCompatibility(JAVA_VERSION)
            androidExtension.compileOptions.targetCompatibility(JAVA_VERSION)
        }

        val kotlinExtension = target.extensions.findByType(KotlinAndroidProjectExtension::class.java)
        if (kotlinExtension != null) {
            kotlinExtension.compilerOptions {
                jvmTarget.set(JVM_TARGET)
                languageVersion.set(KOTLIN_VERSION)
            }
        }
    }

}