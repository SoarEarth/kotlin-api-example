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
    implementation("ch.qos.logback:logback-classic")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("net.postgis:postgis-jdbc:2023.1.0")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    runtimeOnly("org.yaml:snakeyaml")

    kapt("io.micronaut.data:micronaut-data-processor")

    testImplementation(kotlin("test"))
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
        kotlinOptions {
            jvmTarget = "21"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "21"
        }
    }
}
