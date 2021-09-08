# Moonsense Android SDK

## Introduction

This repository includes a sample app that demonstrates the use of the Moonsense SDK for Android.

## TLDR

- Clone this repository.
- Generate a [personal access token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token) on Github with `repo` permissions.
- Add your Github `username` and `token` as a gradle property. The easiest way to do this is to add the following lines to `~/.gradle/gradle.properties`
```gradle
gpr.user=<username goes here>
gpr.key=<token goes here>
```
- Create a public token on the [Moonsense Console](https://console.moonsense.cloud/) for your application.
- Add the public token to the `sample-app` under `MainApplication.kt`.
- Run the `sample-app`.

## Features

The current latest release of the SDK is `0.1.0-alpha2`. The purpose of this release in a nutshell is to introduce the public API for the Android SDK. The release also serves as a good starting point to test out a very early integration of the SDK into applications and libraries. 

The full feature list for this release includes:
- The ability to authenticate with the SDK using the public token provided by the [Moonsense Console](https://console.moonsense.cloud/).
- Start a session to collect accelerometer data from the device.
- Publish the accelerometer data to the Moonsense Cloud.
- Stop all or individual sessions.

## Integration

The library has been hosted using [Github Packages](https://github.com/features/packages). If you are reading this you have access to the repo that hosts the SDK library binary. In order to reference the library you need to reference the github maven repository like so:

```gradle
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/moonsense/moonsense-android-sdk")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
```

Since the access to the maven repository is restricted you will need to include your `username` and [personal access token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token) to reference the SDK library. Details around generating a personal access token can be found [here](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token)). Make sure you grant `repo` access to reference the library. Once you have the `username` and `token` add it to your gradle properties. The easiest way is to add the following lines to `~/.gradle/gradle.properties`:

```gradle
gpr.user=<username goes here>
gpr.key=<token goes here>
```

Alternatively you could add the `username` and `password` to your gradle config using [build environment variables](https://docs.gradle.org/current/userguide/build_environment.html). 

With the credentials in place your are ready to include the following line to add the SDK dependency to your app or module:


```gradle
implementation("io.moonsense:android-sdk:0.1.0-alpha2")
```

Note that the SDK also depends on the square wire runtime. We have to manually add it in as dependency as well:

```gradle
implementation("com.squareup.wire:wire-runtime:3.7.0")
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
