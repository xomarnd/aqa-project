object LibVers {
    const val kotlin_coroutines_reactor = "1.6.4"
    const val kotlin_coroutines_core = "1.6.4"
    const val kotlin_logging = "3.0.2"
    const val kotlinx_serialization = "1.4.1"

    const val aws_java_sdk_s3 = "1.12.342"

    const val junit_jupiter = "5.9.1"

    const val kotest = "5.5.1"

    const val faker = "1.0.2"

    const val playwright = "1.28.1"
    const val testng = "7.3.0"
    const val selenide = "6.12.3"

    const val testcontainers = "1.17.6"

}

object Libs {
    // Kotlin
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Global.kotlin_version}"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Global.kotlin_version}"
    const val kotlin_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVers.kotlin_coroutines_core}"
    const val kotlin_coroutines_jdk8 = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${LibVers.kotlin_coroutines_core}"
    const val kotlin_coroutines_reactor =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${LibVers.kotlin_coroutines_reactor}"
    const val kotlinx_serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibVers.kotlinx_serialization}"

    // S3
    const val aws_java_sdk_s3 = "com.amazonaws:aws-java-sdk-s3:${LibVers.aws_java_sdk_s3}"

    // Tests
    const val junit_jupiter = "org.junit.jupiter:junit-jupiter:${LibVers.junit_jupiter}"
    const val junit_engine = "org.junit.jupiter:junit-jupiter-engine:${LibVers.junit_jupiter}"
    const val kotest_assertions_core_jvm: String = "io.kotest:kotest-assertions-core-jvm:${LibVers.kotest}"
    const val faker = "com.github.javafaker:javafaker:${LibVers.faker}"

    const val playwright = "com.microsoft.playwright:playwright:${LibVers.playwright}"
    const val testng = "org.testng:testng:${LibVers.testng}"

    const val selenide = "com.codeborne:selenide:${LibVers.selenide}"
    const val selenide_proxy = "com.codeborne:selenide-proxy:${LibVers.selenide}"

    // Testcontainers
    const val testcontainers_localstack = "org.testcontainers:localstack:${LibVers.testcontainers}"
    const val testcontainers_junit_jupiter = "org.testcontainers:junit-jupiter:${LibVers.testcontainers}"
}

object PluginVers {
    const val kotlin = Global.kotlin_version
    const val update_dependencies = "0.42.0"
    const val detekt = "1.21.0"
    const val kotlin_jvm = Global.kotlin_version
    const val serialization = Global.kotlin_version

    const val allure = "2.11.2"
}

object Plugins {
    const val java = "java"
    const val kotlin = "org.jetbrains.kotlin.jvm"
    const val update_dependencies = "com.github.ben-manes.versions"
    const val java_test_fixtures = "java-test-fixtures"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detekt_formatting = "io.gitlab.arturbosch.detekt:detekt-formatting"
    const val kotlin_jvm = "org.jetbrains.kotlin.jvm"
    const val serialization = "plugin.serialization"

    const val allure = "io.qameta.allure"
}
