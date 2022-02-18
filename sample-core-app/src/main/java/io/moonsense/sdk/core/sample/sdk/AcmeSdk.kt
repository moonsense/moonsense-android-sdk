/*
 * Copyright (c) 2022 Moonsense, Inc. All rights reserved.
 * Created by rahul on 2/11/22, 2:33 PM
 */

package io.moonsense.sdk.core.sample.sdk

import android.content.Context
import android.util.Log
import io.moonsense.models.v2.Bundle
import io.moonsense.sdk.Moonsense
import io.moonsense.sdk.callback.MoonsenseCoreCallback
import io.moonsense.sdk.config.SDKConfig
import io.moonsense.sdk.config.SensorType
import io.moonsense.sdk.exception.MoonsenseException
import io.moonsense.sdk.model.Session
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

/**
 * This SDK is a simple wrapper around the
 * Moonsense Core SDK.
 *
 * This SDK demonstrates the use of the
 * Core SDK including the Session state management.
 *
 * In this case we associate a Moonsense Session with
 * a corresponding Acme Session.
 *
 * This example also demonstrates the handling of
 * the Bundle data received via the [MoonsenseCoreCallback].
 */
class AcmeSdk(context: Context) {

    /**
     * Maps a Moonsense Session to an [AcmeSession].
     */
    private val sessionMap = mutableMapOf<String, AcmeSession>()

    // connect to the acme server
    private val httpUrl = HttpUrl.Builder()
        .scheme("http")
        .host("localhost")
        .port(SERVER_PORT)
        .build()

    private val acmeService = Retrofit.Builder()
        .baseUrl(httpUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(AcmeService::class.java)

    private val moonsenseCoreCallback = object : MoonsenseCoreCallback {

        override fun onBundleCreated(session: Session, bundle: Bundle) {
            // retrieve the Acme Session
            val acmeSession = sessionMap[session.getLocalId()]
            checkNotNull(acmeSession)

            // copy over the required data
            val acmeRequest = AcmeRequest(
                acmeSessionId = acmeSession.sessionId,
                moonsenseSessionId = session.getLocalId(),
                bundle.accelerometer_data.map {
                    Triple(it.x, it.y, it.z)
                }
            )
            // hand over the data to a separate thread
            uploadData(acmeRequest)
        }

        override fun onError(ex: MoonsenseException) {
            Log.e(TAG, "Moonsense error reported $ex", ex)
        }

        override fun onSessionStarted(session: Session) {
            // not used, AcmeSession created when
            // record() is called.
        }

        override fun onSessionStopped(session: Session) {
            val moonsenseLocalId = session.getLocalId()
            // remove the AcmeSession entry
            val acmeSession = sessionMap[moonsenseLocalId]
            Log.d(TAG, "Moonsense Session with id $moonsenseLocalId stopped.")
            Log.d(TAG, "Shutting down Acme Session with id ${acmeSession?.sessionId.orEmpty()}")
            sessionMap.remove(moonsenseLocalId)
        }

        private fun uploadData(acmeRequest: AcmeRequest) {
            val response = acmeService.uploadData(acmeRequest)
            response.enqueue(object : Callback<AcmeResponse> {
                override fun onResponse(
                    call: Call<AcmeResponse>,
                    acmeResponse: Response<AcmeResponse>
                ) {
                    Log.d(TAG, "Total samples uploaded: ${acmeResponse.body()?.count}")
                }

                override fun onFailure(call: Call<AcmeResponse>, t: Throwable) {
                    Log.e(TAG, "Error uploading data $t", t)
                }
            })
        }
    }

    init {
        Moonsense.initialize(
            context,
            SDKConfig(
                // generate a bundle every second,
                bundleGenerationInterval = ONE_SECOND_IN_MILLIS,
                // only record accelerometer data
                sensorTypes = listOf(SensorType.ACCELEROMETER)
            ),
            moonsenseCoreCallback
        )
    }

    /**
     * Record an [AcmeSession].
     *
     * Internally we also start a Moonsense Session.
     */
    fun record(): AcmeSession {
        val acmeSession = AcmeSession(sessionId = UUID.randomUUID().toString())
        val moonsenseSession = Moonsense.startSession(TEN_SECONDS_IN_MILLIS)
        val moonsenseLocalId = moonsenseSession.getLocalId()
        Log.d(TAG, "Moonsense Session created with id $moonsenseLocalId")
        // associate the AcmeSession in the map
        sessionMap[moonsenseLocalId] = acmeSession
        return acmeSession
    }

    companion object {
        private const val TAG = "AcmeSdk"
        private const val ONE_SECOND_IN_MILLIS = 1000L
        private const val TEN_SECONDS_IN_MILLIS = 10_000L
        private const val SERVER_PORT = 3100
    }
}