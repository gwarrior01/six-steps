import org.jooq.meta.kotlin.*
import org.testcontainers.containers.PostgreSQLContainer

plugins {
    alias(libs.plugins.micronaut.application)
    alias(libs.plugins.gradleup.shadow)
    id("nu.studer.jooq") version "10.1"
    id("org.flywaydb.flyway") version "11.8.0"
}

version = "1.0.1"
group = "micronaut"

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.testcontainers:postgresql:1.20.6")
        classpath("org.flywaydb:flyway-database-postgresql:10.4.0")
        classpath("org.postgresql:postgresql:42.7.1")
    }
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.kafka:micronaut-kafka")
    implementation("commons-codec:commons-codec:1.18.0")
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly(libs.postgresql)
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.data:micronaut-data-tx-hibernate")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut.sql:micronaut-jooq")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.awaitility:awaitility:4.3.0")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.testcontainers:junit-jupiter:1.20.6")
    testImplementation("org.testcontainers:postgresql:1.20.6")
    testImplementation("org.testcontainers:kafka:1.20.6")
    testImplementation("org.testcontainers:testcontainers:1.20.6")
    jooqGenerator("org.postgresql:postgresql:42.7.1")
}

application {
    mainClass = "com.example.MicronautApplication"
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}

configurations.create("flywayMigration")

val postgresContainer by tasks.registering {
    doFirst {
        val container = PostgreSQLContainer<Nothing>("postgres:16.1").apply {
            withDatabaseName("tech")
            start()
        }

        project.extra["jdbcUrl"] = container.jdbcUrl
        project.extra["username"] = container.username
        project.extra["password"] = container.password
        project.extra["databaseName"] = container.databaseName
        project.extra["postgresContainer"] = container
    }
}

flyway {
    configurations = listOf("flywayMigration").toTypedArray()
    url = ""
    user = ""
    password = ""
}

jooq {
    configurations {
        create("main") {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

            jooqConfiguration {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc {
                    driver = "org.postgresql.Driver"
                    url = ""
                    user = ""
                    password =  ""

                }
                generator {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"
                        forcedTypes {
                            forcedType {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "JSONB?"
                            }
                            forcedType {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "INET"
                            }
                        }
                    }
                    generate {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target {
                        packageName = "tech.best"
                        directory = "build/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named("flywayMigrate") {
    dependsOn(postgresContainer)
    doFirst {
        flyway.apply {
            url = project.extra["jdbcUrl"] as String
            user = project.extra["username"] as String
            password = project.extra["password"] as String
        }
    }
}

tasks.named("generateJooq").configure {
    dependsOn("postgresContainer")
    dependsOn("flywayMigrate")

    doFirst {
        val config = jooq.configurations.getByName("main")
        val jdbc = config.jooqConfiguration.jdbc

        jdbc.url = project.extra["jdbcUrl"] as String
        jdbc.user = project.extra["username"] as String
        jdbc.password = project.extra["password"] as String
    }

    inputs.files(fileTree("src/main/resources/db/migration")).apply {
        withPropertyName("migrations")
        withPathSensitivity(PathSensitivity.RELATIVE)
    }

    outputs.upToDateWhen { false }

    doLast {
        val container = project.extra["postgresContainer"] as PostgreSQLContainer<*>
        container.stop()
    }
}