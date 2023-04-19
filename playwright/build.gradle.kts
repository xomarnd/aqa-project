plugins {
    `kotlin-dsl`
    id(Plugins.allure) version (PluginVers.allure)
}


dependencies {
    testImplementation(Libs.junit_jupiter)
    testImplementation(project(mapOf("path" to ":common:test-utils")))
    testRuntimeOnly(Libs.junit_engine)

    implementation(Libs.playwright)
    testImplementation(Libs.testng)

    testImplementation(project(mapOf("path" to ":common:s3")))
}

tasks.getByName<Test>("test") {
    useTestNG()
}