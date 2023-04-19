project.base.archivesName.set("common-s3")

plugins {
    `kotlin-dsl`
    id(Plugins.kotlin_jvm) version PluginVers.kotlin_jvm
    kotlin(Plugins.serialization) version PluginVers.serialization apply true
}

dependencies {
    // Kotlin
    implementation(Libs.kotlin_stdlib)
    implementation(Libs.kotlin_reflect)
    implementation(Libs.kotlinx_serialization)

    // S3
    implementation(Libs.aws_java_sdk_s3)

    // Tests
    testFixturesImplementation(project(":common:test-utils"))

    testImplementation(Libs.junit_jupiter)
    testImplementation(Libs.kotest_assertions_core_jvm)
    testImplementation(Libs.testcontainers_localstack)
    testImplementation(Libs.testcontainers_junit_jupiter)
}