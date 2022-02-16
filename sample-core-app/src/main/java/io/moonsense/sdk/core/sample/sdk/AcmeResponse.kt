/*
 * Copyright (c) 2022 Moonsense, Inc. All rights reserved.
 * Created by rahul on 2/14/22, 2:03 PM
 */

package io.moonsense.sdk.core.sample.sdk

/**
 * Returns a result of the data upload.
 */
data class AcmeResponse(
    /**
     * Number of samples processed.
     */
    val count: Int
)