object LibVers {
    const val kotlin_coroutines_reactor = "1.6.4"
    const val kotlin_coroutines_core = "1.6.4"
    const val kotlin_logging = "3.0.2"

    const val junit_jupiter = "5.9.1"

    const val kotest = "5.5.1"

    const val faker = "1.0.2"
}

object Libs {
    // Kotlin
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Global.kotlin_version}"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Global.kotlin_version}"
    const val kotlin_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVers.kotlin_coroutines_core}"
    const val kotlin_coroutines_jdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${LibVers.kotlin_coroutines_core}"
    const val kotlin_coroutines_reactor =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${LibVers.kotlin_coroutines_reactor}"

    // Tests
    const val junit_jupiter = "org.junit.jupiter:junit-jupiter:${LibVers.junit_jupiter}"
    const val kotest_assertions_core_jvm: String = "io.kotest:kotest-assertions-core-jvm:${LibVers.kotest}"
    const val faker = "com.github.javafaker:javafaker:${LibVers.faker}"
}

object PluginVers {
    const val kotlin = Global.kotlin_version
    const val update_dependencies = "0.42.0"
    const val detekt = "1.21.0"
}

object Plugins {
    const val java = "java"
    const val kotlin = "org.jetbrains.kotlin.jvm"
    const val update_dependencies = "com.github.ben-manes.versions"

    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detekt_formatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
}
