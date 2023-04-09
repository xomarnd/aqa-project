import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_JAVA
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_SRC_DIR_KOTLIN
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_JAVA
import io.gitlab.arturbosch.detekt.extensions.DetektExtension.Companion.DEFAULT_TEST_SRC_DIR_KOTLIN
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.kotlin) version PluginVers.kotlin apply false
    id(Plugins.update_dependencies) version PluginVers.update_dependencies
    id(Plugins.detekt) version PluginVers.detekt
}

subprojects {

    apply {
        plugin(Plugins.java)
        plugin(Plugins.kotlin)
        plugin(Plugins.update_dependencies)
        plugin(Plugins.detekt)
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    detekt {
        config = files("$rootDir/detekt/detekt-config.yml")
        buildUponDefaultConfig = true
        autoCorrect = true

        @Suppress("DEPRECATION")
        reports {
            html.required.set(true)
        }

        source = files(
            DEFAULT_SRC_DIR_JAVA,
            DEFAULT_TEST_SRC_DIR_JAVA,
            DEFAULT_SRC_DIR_KOTLIN,
            DEFAULT_TEST_SRC_DIR_KOTLIN,
            "src/testFixtures/kotlin",
        )

        dependencies {
            detektPlugins("${Plugins.detekt_formatting}:${PluginVers.detekt}")
        }
    }

    tasks {
        val check = named<DefaultTask>("check")
        val dependencyUpdate = named<DependencyUpdatesTask>("dependencyUpdates")

        check {
            finalizedBy(dependencyUpdate)
        }

        dependencyUpdate {
            revision = "release"
            outputFormatter = "txt"
            checkForGradleUpdate = true
            outputDir = "$buildDir/reports/dependencies"
            reportfileName = "updates"
        }

        dependencyUpdate.configure {
            fun isNonStable(version: String): Boolean {
                val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
                val regex = "^[0-9,.v-]+(-r)?$".toRegex()
                val isStable = stableKeyword || regex.matches(version)
                return isStable.not()
            }

            rejectVersionIf {
                isNonStable(candidate.version) && !isNonStable(currentVersion)
            }
        }

        val failOnWarning =
            project.properties["allWarningsAsErrors"] != null && project.properties["allWarningsAsErrors"] == "true"

        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                jvmTarget = JavaVersion.VERSION_11.toString()
                allWarningsAsErrors = failOnWarning
                freeCompilerArgs = listOf("-Xjvm-default=enable")
            }
        }

        withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:all")
        }

        withType<Test> {
            useJUnitPlatform()

            testLogging {
                events(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                showStandardStreams = true
                exceptionFormat = TestExceptionFormat.FULL
            }

            systemProperties["pact.rootDir"] = "${rootProject.buildDir}/pacts"
        }
    }

    /**
     * Фикс ошибки `Could not find snakeyaml-1.30-android.jar (org.yaml:snakeyaml:1.30).`
     */
    configurations.all {
        resolutionStrategy.eachDependency {
            if(requested.module.toString() == "org.yaml:snakeyaml") {
                artifactSelection {
                    selectArtifact(DependencyArtifact.DEFAULT_TYPE, null, null)
                }
            }
        }
    }
}
