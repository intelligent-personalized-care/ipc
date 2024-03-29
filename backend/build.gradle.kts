import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.21"
    id("org.springframework.boot") version "2.7.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "pt.ipc"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven {
        url = uri("https://storage.googleapis.com/cloud-sql-jdbc")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("net.glxn:qrgen:1.4")

    implementation("com.zaxxer:HikariCP:4.0.3")

    implementation("com.google.cloud.sql:postgres-socket-factory:1.2.2")
    implementation("org.postgresql:postgresql:42.2.27")

    // Spring dependencies
    implementation("org.springframework:spring-context:5.3.22")
    implementation("org.springframework.security:spring-security-core:5.7.7")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-cache:2.4.0")


    // JDBI dependencies
    implementation("org.jdbi:jdbi3-core:3.35.0")
    implementation("org.jdbi:jdbi3-kotlin:3.35.0")
    implementation("org.jdbi:jdbi3-postgres:3.33.0")
    implementation("org.postgresql:postgresql:42.5.1")

    // JWT implementation
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Google Storage
    implementation("com.google.cloud:google-cloud-storage:1.118.1")

    // Increase request maximum permited size
    implementation("commons-fileupload:commons-fileupload:1.4")

    // Jetty Dependencies
    implementation("org.eclipse.jetty:jetty-server:11.0.13")
    implementation("org.eclipse.jetty:jetty-servlet:11.0.13")

    implementation("org.slf4j:slf4j-api:2.0.5")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
