/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/16/21, 9:01 AM
 */

package io.moonsense.sample.payment.app

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import io.moonsense.sample.payment.app.widget.CookieItemView
import io.moonsense.sample.payment.sdk.PaymentDialog
import io.moonsense.sample.payment.sdk.PaymentListener
import java.util.Locale
import kotlin.math.roundToLong
import android.view.Gravity
import androidx.core.content.ContextCompat

internal class MainActivity : FragmentActivity(), () -> Unit {

    private lateinit var chocoChipView: CookieItemView
    private lateinit var oatmealRaisinView: CookieItemView
    private lateinit var subtotalPriceView: TextView
    private lateinit var taxPriceView: TextView
    private lateinit var totalPriceView: TextView
    private lateinit var buyButton: View
    private lateinit var parentLayout: ViewGroup
    private var totalPrice: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentLayout = findViewById(R.id.parent_layout)
        buyButton = findViewById(R.id.buy_button)
        chocoChipView = findViewById(R.id.choco_chip_row)
        oatmealRaisinView = findViewById(R.id.oatmeal_raisin_row)
        subtotalPriceView = findViewById(R.id.subtotal_price)
        taxPriceView = findViewById(R.id.tax_price)
        totalPriceView = findViewById(R.id.total_price)
        chocoChipView.setTitle(R.string.choco_chip)
        oatmealRaisinView.setTitle(R.string.oatmeal_raisin)
        initializeView()
    }

    private fun initializeView() {
        chocoChipView.setOnQuantityChangedListener(this)
        oatmealRaisinView.setOnQuantityChangedListener(this)

        chocoChipView.setPrice(CHOCO_CHIP_COOKIE_PRICE)
        oatmealRaisinView.setPrice(OATMEAL_RAISIN_COOKIE_PRICE)
        chocoChipView.setQuantity(1)
        oatmealRaisinView.setQuantity(1)

        buyButton.setOnClickListener {
            val price = (totalPrice * ONE_HUNDRED).roundToLong() / ONE_HUNDRED
            PaymentDialog.launch(
                this,
                price,
                object : PaymentListener {
                    override fun onComplete() {
                        showToast("Payment Complete!", R.color.background_green, R.color.black)
                    }

                    override fun onDismissed() {
                        showToast("Payment Incomplete!", R.color.background_red, R.color.red)
                    }
                }
            )
        }
    }

    private fun showToast(message: String, snackBarBgColorRes: Int, textColorRes: Int) {
        val snackBar = Snackbar.make(this, parentLayout, message, Snackbar.LENGTH_SHORT)
        val view = snackBar.view
        view.setBackgroundColor(ContextCompat.getColor(this, snackBarBgColorRes))
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, textColorRes))
        textView.typeface = Typeface.DEFAULT_BOLD
        textView.gravity = Gravity.CENTER_HORIZONTAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        snackBar.show()
    }

    override fun invoke() {
        val subTotal = chocoChipView.getTotalPrice() + oatmealRaisinView.getTotalPrice()
        val tax = subTotal * DEFAULT_TAX_PERCENTAGE
        totalPrice = subTotal + tax
        subtotalPriceView.text = getFormattedPrice(subTotal)
        taxPriceView.text = getFormattedPrice(tax)
        totalPriceView.text = getFormattedPrice(totalPrice)
    }

    private fun getFormattedPrice(value: Float) =
        String.format(Locale.getDefault(), "$ %.2f", value)

    companion object {
        private const val CHOCO_CHIP_COOKIE_PRICE = 4.99f
        private const val OATMEAL_RAISIN_COOKIE_PRICE = 5.99f
        private const val DEFAULT_TAX_PERCENTAGE = 0.0825f
        private const val ONE_HUNDRED = 100f
    }
}