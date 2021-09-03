/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 8/19/21, 10:49 AM
 */

package io.moonsense.sdk.sample

import android.app.Application
import io.moonsense.sdk.Moonsense

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * Note that we choose to call initialize here but
         * it can easily be moved to an Activity in which case
         * the scope of the SDK then will be constrained to
         * the lifecycle of the Activity.
         *
         * The SDK initializes a thread for normal operation.
         * Moving the call to the Activity will lazily
         * initialize the thread. In this case, the thread will
         * remain active for the Activity and other
         * activities it calls. The thread will be immediately
         * shutdown on destruction of all Activities created since
         * making the SDK no operational from that point onwards.
         *
         * Calling it here ensures its operation across
         * the app lifecycle. Do make the determination based
         * on your use case.
         *
         * Finally, we need a public token to make sure
         * that the app can publish data to the Moonsense Cloud.
         * The token can be generated using the Moonsense Console.
         * Refer to the getting started guide for more information
         * around generating tokens.
         */
        Moonsense.initialize(
            this,
            "ADD PUBLIC TOKEN HERE .."
        )
    }
}