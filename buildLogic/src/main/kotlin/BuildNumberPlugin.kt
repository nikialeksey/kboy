import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.time.Instant
import java.time.ZoneOffset

class BuildNumberPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.configure(ApplicationExtension::class.java) {
            defaultConfig {
                versionCode = getBuildNumber()
            }
        }
    }

    private fun getBuildNumber(): Int {
        return getAdjustedTimestamp()
    }

    private fun getAdjustedTimestamp(): Int {
        return Instant.now()
            .atZone(ZoneOffset.UTC)
            .toEpochSecond()
            .toInt()
    }
}