/*
 * Copyright (c) 2022 Moonsense, Inc. All rights reserved.
 * Created by rahul on 2/11/22, 4:42 PM
 */

package io.moonsense.sdk.core.sample.sdk

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Implement an interface to communicate
 * with the Acme Server.
 */
interface AcmeService {

    /**
     * Uploads data to the Acme server.
     *
     * Returns the count of accelerometer samples
     * processed.
     */
    @POST("/v1/data/")
    fun uploadData(@Body request: AcmeRequest): Call<AcmeResponse>
}