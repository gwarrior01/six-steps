plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.graalvm.native)
}
group = "spring-boot"
version = "1.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

graalvmNative {
    binaries.all {
        buildArgs.add("--no-fallback")
        buildArgs.add("-Ob")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.micrometer.prometheus)
    implementation(libs.spring.kafka)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.spring.boot.docker.compose)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.bundles.testcontainers)
    testImplementation(libs.rest.assured)
    testImplementation(libs.awaitility)
}