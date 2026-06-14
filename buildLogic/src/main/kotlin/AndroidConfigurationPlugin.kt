import com.alexeycode.kboy.libs
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AndroidConfigurationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val kmpExtension = target.extensions.findByType(KotlinMultiplatformExtension::class.java)
        if (kmpExtension != null) {
            kmpExtension.targets.onEach { kotlinTarget ->
                if (kotlinTarget is KotlinMultiplatformAndroidLibraryTarget) {
                    kotlinTarget.compileSdk {
                        version = release(
                            target.libs.findVersion("android-compileSdk").get().toString().toInt()
                        )
                    }
                    kotlinTarget.minSdk {
                        version = release(
                            target.libs.findVersion("android-minSdk").get().toString().toInt()
                        )
                    }
                }
            }
        }

        val androidExtension = target.extensions.findByType(CommonExtension::class.java)
        if (androidExtension != null) {
            androidExtension.compileSdk {
                version = release(
                    target.libs.findVersion("android-compileSdk").get().toString().toInt()
                )
            }
            androidExtension.defaultConfig.minSdk {
                version = release(
                    target.libs.findVersion("android-minSdk").get().toString().toInt()
                )
            }
        }
        val androidApplicationExtension = target.extensions.findByType(ApplicationExtension::class.java)
        if (androidApplicationExtension != null) {
            androidApplicationExtension.defaultConfig.targetSdk {
                version = release(
                    target.libs.findVersion("android-targetSdk").get().toString().toInt()
                )
            }
        }
    }

}