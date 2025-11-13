plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.kapt") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.0.0"
    id("io.micronaut.application") version "4.5.0"
}

version = "0.1"
group = "com.example"

micronaut {
    version("4.5.0")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("net.postgis:postgis-jdbc:2023.1.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("org.yaml:snakeyaml")

    kapt("io.micronaut.data:micronaut-data-processor")
    kapt("io.micronaut:micronaut-http-validation")

    testImplementation(kotlin("test"))
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("com.h2database:h2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

allOpen {
    annotation("io.micronaut.data.annotation.MappedEntity")
    annotation("io.micronaut.http.annotation.Controller")
}

application {
    mainClass.set("com.example.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    test {
        useJUnitPlatform()
    }
}
