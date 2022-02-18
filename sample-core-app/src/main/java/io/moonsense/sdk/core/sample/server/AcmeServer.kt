/*
 * Copyright (c) 2022 Moonsense, Inc. All rights reserved.
 * Created by rahul on 2/11/22, 2:35 PM
 */

package io.moonsense.sdk.core.sample.server

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import io.moonsense.sdk.core.sample.sdk.AcmeRequest
import io.moonsense.sdk.core.sample.sdk.AcmeResponse
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.net.InetAddress
import java.nio.charset.Charset

/**
 * Implements a simple local server.
 */
class AcmeServer(private val acmeServerListener: AcmeServerListener) : Handler.Callback {
    private val gson = Gson()
    private val mockWebServer = MockWebServer()

    private val handlerThread = HandlerThread(TAG)
    private var handler: Handler? = null

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.path == "/v1/data/") {
                val bodyString = request.body.readString(Charset.defaultCharset())

                val acmeRequest = gson.fromJson(bodyString, AcmeRequest::class.java)

                // process data

                acmeServerListener.onDataReceived(
                    acmeRequest.acmeSessionId,
                    acmeRequest.accelerometerData.size
                )

                val acmeResponse = AcmeResponse(acmeRequest.accelerometerData.size)

                try {
                    return MockResponse()
                        .setResponseCode(HTTP_CODE_OK)
                        .setBody(gson.toJson(acmeResponse))
                } catch (e: IOException) {
                    Log.d(TAG, "Exception is $e")
                }
            }
            return MockResponse().setResponseCode(HTTP_CODE_NOT_FOUND)
        }
    }

    init {
        mockWebServer.dispatcher = dispatcher
    }

    /**
     * Starts the acme server.
     */
    fun start() {
        handlerThread.start()
        handler = Handler(handlerThread.looper, this)
        handler?.sendEmptyMessage(MSG_START_SERVER)
    }

    /**
     * Stops the acme server.
     */
    fun stop() {
        handler?.sendEmptyMessage(MSG_START_SERVER)
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            MSG_START_SERVER ->
                mockWebServer.start(InetAddress.getLocalHost(), SERVER_PORT)
            MSG_STOP_SERVER -> {
                mockWebServer.shutdown()
                handlerThread.quit()
            }
            else ->
                return false
        }
        return true
    }

    companion object {
        private const val TAG = "AcmeServer"
        private const val HTTP_CODE_NOT_FOUND = 404
        private const val HTTP_CODE_OK = 200
        private const val MSG_START_SERVER = 1
        private const val MSG_STOP_SERVER = 2
        private const val SERVER_PORT = 3100
    }

    /**
     * Used to notify in case of data received by the
     * server.
     */
    interface AcmeServerListener {
        /**
         * Called when data is received on
         * an Acme session. Returns the count of samples
         * received along with the acme session id.
         */
        fun onDataReceived(sessionId: String, count: Int)
    }
}