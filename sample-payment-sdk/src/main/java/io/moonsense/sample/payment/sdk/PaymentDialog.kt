/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/16/21, 4:08 PM
 */

package io.moonsense.sample.payment.sdk

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import io.moonsense.sample.payment.sdk.widget.SwipeToBuyView
import io.moonsense.sdk.Moonsense
import io.moonsense.sdk.model.Session
import java.util.concurrent.TimeUnit
import com.google.android.material.bottomsheet.BottomSheetBehavior

import android.widget.FrameLayout
import java.lang.StringBuilder

/**
 * The dialog that is shown to the user with
 * the details of the payment and a swipe to buy widget.
 */
class PaymentDialog(private val paymentListener: PaymentListener) :
    BottomSheetDialogFragment() {

    private var session: Session? = null
    private lateinit var swipeToBuyView: SwipeToBuyView

    private var isCardValid = false
    private var isDateValid = false
    private var isCvcValid = false

    private val cardTextWatcher = object : PaymentTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            s ?: return
            if (isFormatted(s, CARD_MAX_CHARS, CARD_SEPARATOR_MOD, CARD_SEPARATOR)) {
                isCardValid = s.length == CARD_MAX_CHARS
                shouldEnableSwipeToBuy()
                return
            }
            val numbers = s.take(CARD_MAX_CHARS).filter(Character::isDigit)
            s.replace(
                0,
                s.length,
                getFormattedString(numbers, CARD_SEPARATOR_MOD, CARD_SEPARATOR)
            )
        }
    }

    private val dateTextWatcher = object : PaymentTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            s ?: return

            // make sure that the first character is correct
            if (s.length == 1 && !(s[0] == '0' || s[0] == '1')) {
                s.clear()
                return
            }

            // second character can only be one of 0, 1 or 2
            if (s.length == 2 && s[0] == '1' && !isSecondDateCharValid(s)) {
                s.replace(0, 2, s[0].toString())
                return
            }

            // prevent 00
            if (s.length == 2 && s[0] == '0' && s[1] == '0') {
                s.replace(0, 2, s[0].toString())
                return
            }

            if (isFormatted(s, DATE_MAX_CHARS, DATE_SEPARATOR_MOD, DATE_SEPARATOR)) {
                isDateValid = s.length == DATE_MAX_CHARS
                shouldEnableSwipeToBuy()
                return
            }
            val numbers = s.take(DATE_MAX_CHARS).filter(Character::isDigit)
            s.replace(
                0,
                s.length,
                getFormattedString(numbers, DATE_SEPARATOR_MOD, DATE_SEPARATOR)
            )
        }
        private fun isSecondDateCharValid(s: CharSequence) = s[1] == '0' || s[1] == '1' || s[1] == '2'
    }

    private val cvcTextWatcher = object : PaymentTextWatcher() {

        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            s ?: return

            isCvcValid = s.length == MAX_CVC_CHARS
            shouldEnableSwipeToBuy()
            if (s.length > MAX_CVC_CHARS) {
                s.delete(MAX_CVC_CHARS, s.length)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_payment_dialog, container, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val totalPrice = view.findViewById<TextView>(R.id.total_price)
        swipeToBuyView = view.findViewById(R.id.swipe_to_buy_view)

        // fully expand the dialog so that it shows correctly when
        // the keyboard is open
        dialog?.setOnShowListener {
            val bottomSheet =
                dialog?.findViewById(R.id.design_bottom_sheet) as FrameLayout
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        session = Moonsense.startSession(
            1,
            TimeUnit.HOURS,
            listOf(PAYMENT_SDK_LABEL)
        )
        arguments?.getFloat(ARG_TOTAL_PRICE)?.let {
            val formattedPrice = "$ $it"
            totalPrice.text = formattedPrice
        }
        view.findViewById<View>(R.id.close_button).setOnClickListener {
            session?.stopSession()
            paymentListener.onDismissed()
            dismiss()
        }

        view.findViewById<EditText>(R.id.card_edit_text).addTextChangedListener(cardTextWatcher)
        view.findViewById<EditText>(R.id.date_edit_text).addTextChangedListener(dateTextWatcher)
        view.findViewById<EditText>(R.id.cvc_edit_text).addTextChangedListener(cvcTextWatcher)

        val swipeToBuyView = view.findViewById<SwipeToBuyView>(R.id.swipe_to_buy_view)
        swipeToBuyView.setOnCompleteRunnable {
            session?.stopSession()
            paymentListener.onComplete()
            dismiss()
        }
    }

    private fun shouldEnableSwipeToBuy() {
        if (isCardValid && isDateValid && isCvcValid) {
            swipeToBuyView.shouldEnable(true)
        } else {
            swipeToBuyView.shouldEnable(false)
        }
    }

    private fun isFormatted(
        s: CharSequence,
        totalLen: Int,
        separatorMod: Int,
        separator: Char
    ): Boolean {
        if (s.length > totalLen) return false
        for (i in 1..s.length) {
            val isSeparatorPosition = i % separatorMod == 0
            if (isSeparatorPosition && s[i - 1] != separator) {
                return false
            }
            if (!isSeparatorPosition && s[i - 1] == separator) {
                return false
            }
        }
        return true
    }

    private fun getFormattedString(
        numbers: CharSequence,
        separatorMod: Int,
        separator: Char
    ): String {
        val stringBuilder = StringBuilder()
        for (i in numbers.indices) {
            val isSpace = (stringBuilder.length + 1) % separatorMod == 0
            if (isSpace) stringBuilder.append(separator)
            stringBuilder.append(numbers[i])
        }
        return stringBuilder.toString()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        session?.stopSession()
        paymentListener.onDismissed()
    }

    /**
     * Helps stub out unnecessary methods.
     */
    private open class PaymentTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // unused
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // unused
        }

        override fun afterTextChanged(s: Editable?) {
            // unused
        }
    }

    companion object {

        private const val PAYMENT_FRAGMENT_TAG = "payment_fragment_dialog"
        private const val ARG_TOTAL_PRICE = "total_price"
        private const val PAYMENT_SDK_LABEL = "android_payment_sdk_sample"

        private const val CARD_MAX_CHARS = 19
        private const val CARD_SEPARATOR_MOD = 5
        private const val CARD_SEPARATOR = ' '

        private const val DATE_MAX_CHARS = 5
        private const val DATE_SEPARATOR_MOD = 3
        private const val DATE_SEPARATOR = '/'

        private const val MAX_CVC_CHARS = 3

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