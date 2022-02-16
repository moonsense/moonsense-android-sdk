/*
 * Copyright (c) 2022 Moonsense, Inc. All rights reserved.
 * Created by rahul on 2/11/22, 2:45 PM
 */

package io.moonsense.sdk.core.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import io.moonsense.sdk.core.sample.sdk.AcmeSdk
import io.moonsense.sdk.core.sample.server.AcmeServer

internal class MainActivity : Activity() {

    private lateinit var acmeServer: AcmeServer
    private lateinit var acmeSdk: AcmeSdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataTextView = findViewById<TextView>(R.id.data)
        acmeServer = AcmeServer(object : AcmeServer.AcmeServerListener {
            override fun onDataReceived(sessionId: String, count: Int) {
                runOnUiThread {
                    dataTextView.append(
                        "Session $sessionId received $count samples\n"
                    )
                }
            }
        })
        acmeServer.start()
        acmeSdk = AcmeSdk(this)
        findViewById<Button>(R.id.record).setOnClickListener {
            acmeSdk.record()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        acmeServer.stop()
    }
}