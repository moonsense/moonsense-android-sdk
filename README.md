# Moonsense Android SDK

## Introduction

The [moonsense-android-sdk](https://github.com/moonsense/moonsense-android-sdk) repository includes sample apps that demonstrate the use of the Moonsense SDK for Android.

## TL;DR

- Clone the [moonsense-android-sdk](https://github.com/moonsense/moonsense-android-sdk) repository.
- Replace the lines in [build.gradle.kts](https://github.com/moonsense/moonsense-android-sdk/blob/main/build.gradle.kts) with the repo access token provided to you:
```groovy
maven {
    val repoAccessToken = "ADD_TOKEN_HERE"
    url = uri("https://dl.moonsense.io/$repoAccessToken/sdk/maven")
}
```
- Create a public token on the [Moonsense Console](https://console.moonsense.cloud/) for your application.
- Add the public token to the `MainApplication` class in `sample-app`.
- Run the `sample-app`.

## Version History

The latest release of the SDK is `1.5.0`. Details about the current and past releases can be found in the [Releases](https://github.com/moonsense/moonsense-android-sdk/releases) section.

## Integration

All that is needed to download the SDK is a repo access token. This token should have been provided to you. In case you do not have one contact [support@moonsense.io](mailto:support@moonsense.io). Once you have the repo access token, you can add it to your maven config in gradle like so:

```groovy
repositories {
    maven {
        val repoAccessToken = "ADD_TOKEN_HERE"
        url = uri("https://dl.moonsense.io/$repoAccessToken/sdk/maven")
    }
}
```

With the repository set up, you can add the SDK dependency to your app/module using the following line:

```groovy
implementation("io.moonsense:android-sdk:<latest_version>")
```

## Usage

The SDK needs to be initialized before use. Use the [initialize()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/initialize.html) method in either the `Application.onCreate()` or `Activity.onCreate()` to prepare the SDK for recording. SDK/Library developers can add [initialize()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/initialize.html) to their SDK initialization routine. The [initialize()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/initialize.html) call expects a `publicToken` generated using the [Moonsense Console](https://console.moonsense.cloud/). In order to obtain a token you need to:

- Have a valid Moonsense account.
- Set up a project for the SDK to use. The default can be used in case you do not want to create one.
- Configure an application for the SDK to record to.
- Once the app is set up, use the `Create token` button to obtain the secret and public token to use. The SDK expects the public token as a part of the [initialize()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/initialize.html) call. You can hold on to the secret token to read from the Moonsense Cloud later.

Once initialized you can use the [startSession()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/start-session.html) and [stopAllSessions()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/stop-all-sessions.html) to start and stop recording sessions respectively. If you need a finer control over stopping individual sessions, the [startSession()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk/-moonsense/start-session.html) call returns a [Session](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk.model/-session/index.html) object that includes a [stopSession()](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk.model/-session/stop-session.html) method.

Additionally, a [MoonsenseCallback](https://android.sdk-docs.moonsense.io/sdk/io.moonsense.sdk.callback/-moonsense-callback/index.html) can be registered to provide the caller with events from the SDK.

The SDK reference can be found at [android.sdk-docs.moonsense.io](https://android.sdk-docs.moonsense.io/).

This repo includes three sample apps:

- `sample-app` - This sample app demonstrates the use of the Moonsense Android SDK within an application context. The example in this case is quite simple and minimal and can serve as a good starting point for developers looking for a quick integration.
- `sample-payment-app` - This sample app demonstrates the library usage of the Moonsense Android SDK. Note that the `sample-payment-app` does not directly depend on the Moonsense Android SDK. Instead it includes a dependency to the `sample-payment-sdk` which then packages the Moonsense Android SDK. The app developer here does not have any visibility into the Moonsense Android SDK as they only interface with the `sample-payment-sdk`. The example is useful for SDK/library developers looking to integrate the Moonsense Android SDK. This sample app comes in two variants, the features variant records a session with the [Features SDK](https://docs.moonsense.io/articles/sdk/feature-generation) and the standard variant does not.
- `sample-core-app` - This sample app shows how to integrate a variation of the Moonsense Android SDK called the Core SDK. For all intents and purposes the standard Android SDK (referred to as the Cloud SDK) should suffice for a majority of use cases. In case you do need specialized use of the Moonsense SDK contact [support@moonsense.io](mailto:support@moonsense.io) for access. Additional information regarding the Core SDK can be found here - [Advanced Usage](https://docs.moonsense.io/articles/sdk/advanced-usage).

## Terms Of Service

This SDK is distributed under the [Moonsense Terms Of Service](https://www.moonsense.io/terms-of-service).

## Support

Feel free to raise an [Issue](https://github.com/moonsense/moonsense-android-sdk/issues) around bugs, usage, concerns or feedback.
