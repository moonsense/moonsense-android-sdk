/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/22/21, 11:59 AM
 */

package io.moonsense.sample.payment.sdk

import android.content.Context
import android.widget.Toast
import io.moonsense.sdk.Moonsense
import io.moonsense.sdk.callback.MoonsenseCallback
import io.moonsense.sdk.exception.MoonsenseException
import io.moonsense.sdk.model.Session

/**
 * The main entry point for the sample
 * payment sdk.
 */
object Payment {

    /**
     * Initializes the payment sdk.
     */
    fun init(context: Context) {
        Moonsense.initialize(
            context,
            "ADD PUBLIC TOKEN HERE ..",
            object : MoonsenseCallback {
                override fun onError(ex: MoonsenseException) {
                    Toast.makeText(
                        context,
                        "Error: msg (${ex.message})",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onSessionStarted(session: Session) {
                    // unused
                }

                override fun onSessionStopped(session: Session) {
                    // unused
                }
            }
        )
    }
}