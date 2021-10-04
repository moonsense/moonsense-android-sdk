/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 8/19/21, 10:49 AM
 */

package io.moonsense.sample.payment.app

import android.app.Application
import io.moonsense.sample.payment.sdk.Payment

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Payment.init(this)
    }
}