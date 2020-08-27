import org.jetbrains.kotlin.gradle.tasks.KotlinCompile



plugins {
    id ("io.gitlab.arturbosch.detekt") version "1.0.0.RC6-3"
    id("org.sonarqube") version ("3.0")
    jacoco
    `java-library`
    kotlin("jvm") version "1.4.0"
    id("me.champeau.gradle.jmh") version "0.5.0"
    id("com.palantir.graal") version "0.7.1-13-gd190241"
}


version = "1.0"

val scriptname: String by project  // read value from gradle.properties

graal {
    mainClass("scripts.${scriptname.capitalize()}Kt")
    outputName(scriptname)     // output is build/graal/${scriptname}
}


detekt {
    version = "1.0.0.RC6-3"
    profile("main") {
        input = "$projectDir"
        config = "$projectDir/default-detekt-config.yml"
        filters = ".*/*Test.*,.*/resources/.*,.*/tmp/.*"
    }
}

jacoco {
    toolVersion = "0.8.5"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("${buildDir}/jacocoHtml")
    }
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("commons-validator:commons-validator:1.6")
    implementation("io.ktor:ktor-client-gson:1.4.0")
    implementation("io.ktor:ktor-client-apache:1.4.0")


    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
    testImplementation(kotlin("test-junit5"))
    implementation(kotlin("script-runtime"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    test {
        useJUnitPlatform {
            maxParallelForks = Runtime.getRuntime().availableProcessors() / 2
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
        suppressWarnings = true
    }
}
