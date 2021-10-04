/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/16/21, 4:08 PM
 */

package io.moonsense.sample.payment.sdk

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import io.moonsense.sample.payment.sdk.widget.SwipeToBuyView
import io.moonsense.sdk.Moonsense
import io.moonsense.sdk.model.Session
import java.util.concurrent.TimeUnit

/**
 * The dialog that is shown to the user with
 * the details of the payment and a swipe to buy widget.
 */
class PaymentDialog(private val paymentListener: PaymentListener) :
    BottomSheetDialogFragment() {

    private var session: Session? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_payment_dialog, container, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val totalPrice = view.findViewById<TextView>(R.id.total_price)
        session = Moonsense.startSession(
            1,
            TimeUnit.HOURS,
            listOf(PAYMENT_SDK_LABEL)
        )
        arguments?.getFloat(ARG_TOTAL_PRICE)?.let {
            val formattedPrice = "$ $it"
            totalPrice.text = formattedPrice
        }
        view.findViewById<TextView>(R.id.card_number).text = "xxxx xxxx xxxx 4521"
        view.findViewById<View>(R.id.close_button).setOnClickListener {
            session?.stopSession()
            paymentListener.onDismissed()
            dismiss()
        }

        val swipeToBuyView = view.findViewById<SwipeToBuyView>(R.id.swipe_to_buy_view)
        swipeToBuyView.setOnCompleteRunnable {
            session?.stopSession()
            paymentListener.onComplete()
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        session?.stopSession()
        paymentListener.onDismissed()
    }

    companion object {

        private const val PAYMENT_FRAGMENT_TAG = "payment_fragment_dialog"
        private const val ARG_TOTAL_PRICE = "total_price"
        private const val PAYMENT_SDK_LABEL = "android_payment_sdk_sample"

        /**
         * Defines a method to launch the [PaymentDialog].
         *
         * @param fragmentActivity requires a [FragmentActivity] since the dialog
         * is a [BottomSheetDialogFragment].
         * @param totalPrice the price to be charged.
         * @param paymentListener the result is shared via this callback.
         */
        fun launch(
            fragmentActivity: FragmentActivity,
            totalPrice: Float,
            paymentListener: PaymentListener
        ) {
            val fragment = PaymentDialog(paymentListener).apply {
                arguments = Bundle().apply {
                    putFloat(ARG_TOTAL_PRICE, totalPrice)
                }
            }
            fragment.show(fragmentActivity.supportFragmentManager, PAYMENT_FRAGMENT_TAG)
        }
    }
}