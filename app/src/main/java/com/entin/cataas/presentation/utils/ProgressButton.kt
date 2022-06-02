package com.entin.cataas.presentation.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.entin.cataas.R
import com.entin.cataas.databinding.ProgressButtonBinding

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null

    private val binding = ProgressButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    private var state: ProgressButtonState = ProgressButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    init {
        setLayout(attrs)
        refreshState()
    }

    fun setLoading() {
        state = ProgressButtonState.Loading
    }

    fun setNormal() {
        state = ProgressButtonState.Normal
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val attributes =
                context.obtainStyledAttributes(attributeSet, R.styleable.ProgressButton)

            setBackgroundResource(R.drawable.button_background)

            val titleResId =
                attributes.getResourceId(R.styleable.ProgressButton_progress_btn_title, 0)
            if (titleResId != 0) {
                title = context.getString(titleResId)
            }

            val loadingTitleResId = attributes
                .getResourceId(R.styleable.ProgressButton_progress_btn_title_loading, 0)
            if (titleResId != 0) {
                loadingTitle = context.getString(loadingTitleResId)
            }

            attributes.recycle()
        }
    }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()

        binding.apply {
            textTitle.run {
                isEnabled = state.isEnabled
                isClickable = state.isEnabled
            }

            progressButton.visibility = state.progressVisibility
        }

        when (state) {
            ProgressButtonState.Loading -> binding.textTitle.text = title
            ProgressButtonState.Normal -> {
                binding.apply {
                    textTitle.text = loadingTitle
                    textTitle.setOnClickListener {
                        this@ProgressButton.callOnClick()
                    }
                }
            }
        }
    }

    sealed class ProgressButtonState(val isEnabled: Boolean, val progressVisibility: Int) {
        object Normal : ProgressButtonState(true, View.GONE)
        object Loading : ProgressButtonState(false, View.VISIBLE)
    }
}