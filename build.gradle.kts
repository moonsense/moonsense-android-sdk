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