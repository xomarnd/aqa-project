plugins {
    `kotlin-dsl`
    id(Plugins.allure) version (PluginVers.allure)
}


dependencies {
    implementation(Libs.playwright)
    testImplementation(Libs.testng)

    testImplementation(project(mapOf("path" to ":common:s3")))
    testImplementation(project(mapOf("path" to ":common:test-utils")))
}

tasks.getByName<Test>("test") {
    useTestNG()
}