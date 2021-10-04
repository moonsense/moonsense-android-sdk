/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/21/21, 12:08 PM
 */

package io.moonsense.sample.payment.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.moonsense.sample.payment.app.R
import java.util.Locale

/**
 * Represents a row of cookie for sale including the
 * name, price and quantity.
 */
internal class CookieItemView @JvmOverloads constructor(
    context: Context,
    xmlAttrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, xmlAttrs, defStyleAttr) {

    private var price: Float = 0.0f
    private var quantity: Int = 1
    private val priceView: TextView
    private val countView: TextView

    private val addButton: View
    private val removeButton: View

    private var onRowQuantityChanged: (() -> Unit)? = null

    init {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.cookie_item_view, this, true)
        priceView = findViewById(R.id.item_row_price)
        countView = findViewById(R.id.item_row_count)
        addButton = findViewById(R.id.item_row_add_button)
        addButton.setOnClickListener {
            setQuantity(quantity + 1)
        }
        removeButton = findViewById(R.id.item_row_remove_button)
        removeButton.setOnClickListener {
            setQuantity(quantity - 1)
        }
    }

    fun setTitle(stringRes: Int) {
        findViewById<TextView>(R.id.item_row_title).text = resources.getString(stringRes)
    }

    fun setPrice(price: Float) {
        this.price = price
    }

    fun setQuantity(quantity: Int) {
        if (quantity < 0) {
            return
        }
        this.quantity = quantity
        countView.text = quantity.toString()
        val priceFormatted = String.format(Locale.getDefault(), "$ %.2f", price * quantity)
        priceView.text = priceFormatted
        onRowQuantityChanged?.invoke()
    }

    fun setOnQuantityChangedListener(listener: () -> Unit) {
        this.onRowQuantityChanged = listener
    }

    fun getTotalPrice() = price * quantity
}