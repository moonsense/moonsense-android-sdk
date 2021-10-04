# Moonsense Android SDK

## Introduction

This repository includes sample apps that demonstrate the use of the Moonsense SDK for Android.

## TLDR

- Clone this repository.
- Generate a [personal access token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token) on Github with `read:packages` permissions.
- Add your Github `username` and `token` as a gradle property. The easiest way to do this is to add the following lines to `~/.gradle/gradle.properties`
```gradle
gpr.user=<username goes here>
gpr.key=<token goes here>
```
- Create a public token on the [Moonsense Console](https://console.moonsense.cloud/) for your application.
- Add the public token to the `sample-app` under `MainApplication.kt`.
- Run the `sample-app`.

## Version History

The latest release of the SDK is `0.1.0-alpha3`. Details about the current and past releases can be found below:

- [0.1.0-alpha3](https://github.com/moonsense/moonsense-android-sdk/releases/tag/0.1.0-alpha3)
- [0.1.0-alpha2](https://github.com/moonsense/moonsense-android-sdk/releases/tag/0.1.0-alpha2)

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

Since the access to the maven repository is restricted you will need to include your `username` and [personal access token](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token) to reference the SDK library. Details around generating a personal access token can be found [here](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token)). Make sure you grant `read:packages` access to reference the library. Once you have the `username` and `token` add it to your gradle properties. The easiest way is to add the following lines to `~/.gradle/gradle.properties`:

```gradle
gpr.user=<username goes here>
gpr.key=<token goes here>
```

Alternatively you could add the `username` and `password` to your gradle config using [build environment variables](https://docs.gradle.org/current/userguide/build_environment.html). 

With the credentials in place your are ready to include the following line to add the SDK dependency to your app or module:


```gradle
implementation("io.moonsense:android-sdk:0.1.0-alpha3")
```

## Usage

The SDK needs to be initialized before it can be used. Use the `initialize()` method in either an `Application.onCreate()`/`Activity.onCreate()` to prepare the SDK for recording. For SDK/library developers you can add `initialize()` to your initialization routine. The `initialize()` call expects a `publicToken` that can be generated using the [Moonsense Console](https://console.moonsense.cloud/). In order to obtain a token you need to:

- Have a valid Moonsense account.
- Set up a project for the SDK to use. The default can be used in case you do not want to create one.
- Configure an application for the SDK to record to.
- Once the app is set up, use the `Create token` button to obtain the secret and public token to use. The SDK expects the public token as a part of the `initialize()` call. You can hold on to the secret token to read from the Moonsense Cloud later.

Once initialized you can use the `startSession()` and `stopAllSessions()` to start and stop recording sessions respectively. If you need a finer control over stopping individual sessions, the `startSession()` call returns a `Session` object that includes a `stopSession()` method.

Additionally a `MoonsenseCallback` can be registered to provide the caller with events from the SDK.

This repo includes two sample apps:

- `sample-app` - This sample app demonstrates the use of the Moonsense Android SDK within an application context. The example in this case is quite simple and minimal and can serve as a good starting point for developers looking for a quick integration.
- `sample-payment-app` - This sample app demonstrates the library usage of the Moonsense Android SDK. Note that the `sample-payment-app` does not directly depend on the Moonsense Android SDK. Instead it includes a dependency to the `sample-payment-sdk` which then packages the Moonsense Android SDK. The app developer here does not have any visibility into the Moonsense Android SDK as they only interface with the `sample-payment-sdk`. The example is useful for SDK/library developers looking to integrate the Moonsense Android SDK.

## Terms Of Service

This SDK is distributed under the [Moonsense Terms Of Service](https://www.moonsense.io/terms-of-service).

## Support

Feel free to raise an [Issue](https://github.com/moonsense/moonsense-android-sdk/issues) around bugs, usage, concerns or feedback.
