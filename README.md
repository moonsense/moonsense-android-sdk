# Moonsense Android SDK

[![Maven Central](https://shields.io/maven-central/v/io.moonsense/android-sdk)](https://search.maven.org/artifact/io.moonsense/android-sdk)

## Introduction

This repository includes a sample app that demonstrates the use of the Moonsense SDK for Android.

## Features

The current latest release of the SDK is `0.1.0-alpha2`. The purpose of this release in a nutshell is to introduce the public API for the Android SDK. The release also serves as a good starting point to test out a very early integration of the SDK into applications and libraries. 

The full feature list for this release includes:
- The ability to authenticate with the SDK using the public token provided by the [Moonsense Console](https://console.moonsense.cloud/).
- Start a session to collect accelerometer data from the device.
- Publish the accelerometer data to the Moonsense Cloud.
- Stop all or individual sessions.

## Integration

The library has been hosted on `mavenCentral`. Make sure that the repo has been added to your gradle config.

```gradle
repositories {
    mavenCentral()
}
```

Include the following line to add the SDK dependency to your app or module:

*Kotlin DSL*

```gradle
implementation("io.moonsense:android-sdk:0.1.0-alpha2")
```

*Groovy*

```gradle
implementation "io.moonsense:android-sdk:0.1.0-alpha2"
```

## Usage

The SDK needs to be initialized before it can be used. Use the `initialize()` method in either an `Application.onCreate()` or `Activity.onCreate()` to prepare the SDK for recording. The `initialize()` call expects a `publicToken` that can be generated using the [Moonsense Console](https://console.moonsense.cloud/). In order to obtain a token you need to:

- Have a valid Moonsense account.
- Set up a project for the SDK to use. The default can be used in case you do not want to create one.
- Configure an application for the SDK to record to.
- Once the app is set up, use the `Create token` button to obtain the secret and public token to use. The SDK expects the public token as a part of the `initialize()` call. You can hold on to the secret token to read from the Moonsense Cloud later.

Once initialized you can use the `startSession()` and `stopAllSessions()` to start and stop recording sessions respectively. If you need a finer control over stopping individual sessions, the `startSession()` call returns a `Session` object that includes a `stopSession()` method.

Additionally a `MoonsenseCallback` can be registered to provide the caller with events from the SDK.

This repo includes a sample app that demonstrates the SDK usage.

## Terms Of Service

This SDK is distributed under the [Moonsense Terms Of Service](https://www.moonsense.io/terms-of-service).

## Support

Feel free to raise an [Issue](https://github.com/moonsense/moonsense-android-sdk/issues) around bugs, usage, concerns or feedback.
