/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/20/21, 2:31 PM
 */

package io.moonsense.sample.payment.sdk

/**
 * The result of launching the [PaymentDialog] is
 * returned via this listener.
 */
interface PaymentListener {

    /**
     * Invoked when the payment has been successful.
     */
    fun onComplete()

    /**
     * Invoked when the fragment has been dismissed
     * either by swiping away or via the close button.
     */
    fun onDismissed()
}