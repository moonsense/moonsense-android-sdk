/*
 * Copyright (c) 2022 Moonsense, Inc. All rights reserved.
 * Created by rahul on 2/11/22, 4:42 PM
 */

package io.moonsense.sdk.core.sample.sdk

/**
 * Represent the Acme version of the
 * data collected from the Moonsense Bundle.
 */
data class AcmeRequest(
    /**
     * Tht id of the [AcmeSession].
     */
    val acmeSessionId: String,
    /**
     * The id of the corresponding Moonsense
     * Session.
     */
    val moonsenseSessionId: String,
    /**
     * For some reason Acme is only interested in
     * Accelerometer data.
     */
    val accelerometerData: List<Triple<Double, Double, Double>>
)