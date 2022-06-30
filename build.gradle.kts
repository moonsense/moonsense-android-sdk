import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradleTools}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.github.ben-manes.versions").version(Versions.gradleVersionsPlugin)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            val repoAccessToken = "ADD_TOKEN_HERE"
            url = uri("https://dl.moonsense.io/$repoAccessToken/sdk/maven")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    outputFormatter = closureOf<com.github.benmanes.gradle.versions.reporter.result.Result> {
        if (outdated.dependencies.isEmpty()) {
            println("Up to date!")
        } else {
            for (dependency in outdated.dependencies) {
                println("${dependency.group} ${dependency.name} ${dependency.version} -> ${dependency.available.release ?: dependency.available.milestone}")
            }
        }
    }
    rejectVersionIf {
        // ignore jacoco - https://github.com/ben-manes/gradle-versions-plugin/issues/534
        isNonStable(candidate.version) || (candidate.group == "org.jacoco") && (candidate.version != currentVersion)
    }
}