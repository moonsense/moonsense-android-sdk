/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 7/16/21, 11:08 AM
 */

package io.moonsense.sdk.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import io.moonsense.sdk.Moonsense
import io.moonsense.sdk.callback.MoonsenseCallback
import io.moonsense.sdk.exception.MoonsenseException
import io.moonsense.sdk.model.Session
import java.util.concurrent.TimeUnit

internal class MainActivity : Activity() {

    private var activeSessionCountLabel: TextView? = null
    private var sessionCount = 0

    private val moonsenseCallback = object : MoonsenseCallback {
        override fun onError(ex: MoonsenseException) {
            Toast.makeText(
                this@MainActivity,
                "Error: msg (${ex.message})",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onSessionStarted(session: Session) {
            sessionCount++
            activeSessionCountLabel?.text = sessionCount.toString()
        }

        override fun onSessionStopped(session: Session) {
            sessionCount--
            activeSessionCountLabel?.text = sessionCount.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionCount = savedInstanceState?.getInt(KEY_RETAINED_SESSION_COUNT, 0) ?: 0
        activeSessionCountLabel = findViewById(R.id.active_session_count)
        activeSessionCountLabel?.text = sessionCount.toString()

        // The initialization of the SDK is handled in MainApplication.kt

        // register the callback for events from the SDK
        Moonsense.setMoonsenseCallback(moonsenseCallback)

        findViewById<Button>(R.id.start_session).setOnClickListener {
            // this call also returns a session object containing
            // additional information about the started session
            Moonsense.startSession(
                DEFAULT_SESSION_DURATION_SECONDS,
                TimeUnit.SECONDS,
                listOf(DEFAULT_SESSION_LABEL)
            )
        }

        findViewById<Button>(R.id.stop_session).setOnClickListener {
            // alternatively you could call session.stopSession()
            // on an individual session
            Moonsense.stopAllSessions()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_RETAINED_SESSION_COUNT, sessionCount)
    }

    companion object {
        private const val DEFAULT_SESSION_DURATION_SECONDS = 30L
        private const val DEFAULT_SESSION_LABEL = "android_sdk_sample"
        private const val KEY_RETAINED_SESSION_COUNT = "session_count"
    }
}