plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = AppConfig.compileSdkVersion
    buildToolsVersion = AppConfig.buildToolsVersion

    defaultConfig {
        applicationId = "io.moonsense.sdk.core.sample"
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.androidTestInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
}

dependencies {
    implementation("io.moonsense:android-core-sdk:${AppConfig.versionName}")
    implementation("com.squareup.wire:wire-runtime:${Versions.squareWireRuntime}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")
    implementation("com.google.code.gson:gson:${Versions.gson}")
    implementation("com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}")
    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.junitAndroidX}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espressoCore}")
}