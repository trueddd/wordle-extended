import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.create

fun DependencyHandler.junitJupiter() = create("org.junit.jupiter", "junit-jupiter", Versions.JUnit)
fun DependencyHandler.junitPlatform() = platform("org.junit:junit-bom:${Versions.JUnit}")
