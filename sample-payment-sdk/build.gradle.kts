/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/16/21, 8:40 AM
 */

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "io.moonsense.sample.payment.sdk"
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
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
    }

    flavorDimensions.add("sample")
    productFlavors {
        create("standard") {
            dimension = "sample"
            isDefault = true
        }

        create("features") {
            dimension = "sample"
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
    implementation("io.moonsense:android-sdk:${AppConfig.versionName}")
    "featuresImplementation"("io.moonsense:android-features-sdk:${Versions.moonsenseFeatures}")
    implementation("androidx.legacy:legacy-support-v4:${Versions.androidXLegacySupport}")
    implementation("androidx.fragment:fragment-ktx:${Versions.fragmentKtx}")
    implementation("com.google.android.material:material:${Versions.material}")
}