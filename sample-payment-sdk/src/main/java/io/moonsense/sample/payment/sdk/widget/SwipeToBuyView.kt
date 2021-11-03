/*
 * Copyright (c) 2021 Moonsense, Inc. All rights reserved.
 * Created by rahul on 9/21/21, 12:08 PM
 */

package io.moonsense.sample.payment.sdk.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.CircularProgressIndicator
import android.widget.TextView
import io.moonsense.sample.payment.sdk.R

/**
 * Component that models a swipe to buy component using a
 * seekbar. This is for demonstration purposes only.
 */
internal class SwipeToBuyView @JvmOverloads constructor(
    context: Context,
    xmlAttrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle
) : FrameLayout(context, xmlAttrs, defStyleAttr) {
    // Keep track of the start progress value when tracking.
    private var startProgress: Int = 0
    private val seekBar: SeekBar

    // enclosing frame layout.
    private val parentLayout: ViewGroup
    private val textView: TextView
    private val disabledOverlay: View
    private val progressIndicator: CircularProgressIndicator
    private val mainThreadHandler = Handler(context.mainLooper)

    // notifies the caller about completion.
    private var onCompleteRunnable: Runnable? = null

    private val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (startProgress == -1 && fromUser) {
                startProgress = progress
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            startProgress = -1
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            val currentProgress = seekBar?.progress ?: 0

            // only trigger completion when over progress was started
            // below 25 and completed above 90.
            if (currentProgress > COMPLETION_PROGRESS_VALUE &&
                startProgress >= 0 &&
                startProgress < MIN_START_PROGRESS_VALUE
            ) {
                setComplete()
            } else {
                seekBar?.progress = 0
            }
        }
    }

    init {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.swipe_to_buy_layout, this, true)
        seekBar = findViewById(R.id.swipe_to_buy_seekbar)
        parentLayout = findViewById(R.id.swipe_to_buy_frame_layout)
        textView = findViewById(R.id.swipe_to_buy_text_view)
        disabledOverlay = findViewById(R.id.swipe_to_buy_disabled_overlay)
        progressIndicator = findViewById(R.id.swipe_to_buy_progress_indicator)
        setProperties()
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
    }

    private fun setProperties() {
        isClickable = false
        val padding = resources.getDimensionPixelOffset(R.dimen.seekbar_padding)
        seekBar.setPadding(padding, 0, padding, 0)
        seekBar.progress = 0
    }

    private fun setComplete() {
        seekBar.thumb = ContextCompat.getDrawable(context, R.drawable.seekbar_thumb_complete)
        textView.setTextColor(ContextCompat.getColor(context, R.color.green))
        seekBar.progress = COMPLETE_PROGRESS
        seekBar.isEnabled = false
        // mimic a completion loading action using delays.
        mainThreadHandler.postDelayed({ showProgressIndicator() }, ACTION_COMPLETE_DELAY)
    }

    private fun showProgressIndicator() {
        parentLayout.visibility = GONE
        progressIndicator.visibility = VISIBLE

        // mimic a completion action after a delay.
        onCompleteRunnable?.let { mainThreadHandler.postDelayed(it, LOADING_PROGRESS_DELAY) }
    }

    fun shouldEnable(value: Boolean) {
        disabledOverlay.visibility = if (value) View.GONE else View.VISIBLE
    }

    fun setOnCompleteRunnable(r: Runnable) {
        this.onCompleteRunnable = r
    }

    companion object {
        private const val ACTION_COMPLETE_DELAY = 750L
        private const val LOADING_PROGRESS_DELAY = 2500L
        private const val COMPLETION_PROGRESS_VALUE = 90
        private const val MIN_START_PROGRESS_VALUE = 25
        private const val COMPLETE_PROGRESS = 100
    }
}