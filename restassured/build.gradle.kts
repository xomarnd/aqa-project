plugins {
    `kotlin-dsl`
    id(Plugins.allure) version (PluginVers.allure)
}

val junitVersion = "5.9.2"
val restAssuredVersion = "5.3.0"
val jacksonVersion = "2.14.2"
val allureVersion = "2.21.0"

configurations.all {
    exclude("org.apache.groovy:groovy-all:4.0.10")
}
dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.apache.groovy:groovy-all:4.0.10")

//    implementation("io.rest-assured:rest-assured:$restAssuredVersion"){
//        exclude(group = "org.apache.groovy:groovy", module = "4.0.6")
//        exclude(group = "org.apache.groovy:groovy-xml", module = "4.0.6")
//    }
//    implementation("io.rest-assured:json-path:$restAssuredVersion"){
//        exclude(group = "org.apache.groovy:groovy", module = "4.0.6")
//        exclude(group = "org.apache.groovy:groovy-xml", module = "4.0.6")
//    }

    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion"){
        exclude(group = "org.apache.groovy:groovy-datetime", module = "4.0.10")
    }
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    testImplementation("io.qameta.allure:allure-junit5:$allureVersion")

    testImplementation("org.slf4j:slf4j-simple:2.0.5")
    implementation("com.typesafe:config:1.4.2")

    implementation("org.assertj:assertj-core:3.24.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
