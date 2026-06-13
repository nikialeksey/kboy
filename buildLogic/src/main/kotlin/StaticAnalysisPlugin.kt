import com.alexeycode.kboy.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

class StaticAnalysisPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.apply(plugin = "io.gitlab.arturbosch.detekt")

        target.extensions.getByType(DetektExtension::class.java).apply {
            autoCorrect = true
            config.setFrom(
                target.file("${target.rootProject.projectDir}/buildLogic/config/detekt/detekt.yml")
            )
            source = target.files(
                "./src/main/",

                "./src/commonMain/",
                "./src/commonTest/",

                "./src/iosMain",
                "./src/jvmMain",
                "./src/webMain",
                "./src/nonAndroid",
            )
        }

        target.tasks.withType<Detekt>().configureEach {
            exclude { it.file.path.contains("build/generated") }
        }

        target.dependencies.add(
            "detektPlugins",
            target.libs.findLibrary("detekt-formatting").get()
        )

        target.tasks.register("staticAnalysis") {
            setDependsOn(
                mutableListOf<String>()
                    .apply {
                        add("detekt")

                        if (target.tasks.findByName("detektJvmMain") != null) {
                            add("detektJvmMain")
                            add("detektJvmTest")
                        }

                        if (target.tasks.findByName("detektMain") != null) {
                            add("detektMain")
                        }

                        if (target.tasks.findByName("detektIosArm64Main") != null) {
                            add("detektIosArm64Main")
                            add("detektIosArm64Test")
                        }

                        if (target.tasks.findByName("detektWasmJsMain") != null) {
                            add("detektWasmJsMain")
                            add("detektWasmJsTest")
                        }
                    }
            )
        }
    }
}