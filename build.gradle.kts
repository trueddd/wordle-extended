import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Versions.Kotlin
    id("org.jetbrains.compose") version "1.0.1-rc2"
}

group = Config.Windows.PackageName
version = Config.Version

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)

    testImplementation(junitPlatform())
    testImplementation(junitJupiter())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs + listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
    )
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = Config.PackageName
            packageVersion = Config.Version
        }
    }
}