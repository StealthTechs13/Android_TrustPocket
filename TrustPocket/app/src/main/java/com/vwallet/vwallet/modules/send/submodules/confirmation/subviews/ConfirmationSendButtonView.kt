package com.vwallet.vwallet.modules.send.submodules.confirmation.subviews

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.vwallet.vwallet.R
import com.vwallet.vwallet.modules.send.submodules.confirmation.SendConfirmationModule
import kotlinx.android.synthetic.main.view_button_with_progressbar.view.*

class ConfirmationSendButtonView : ConstraintLayout {

    init {
        inflate(context, R.layout.view_button_with_progressbar, this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(state: SendConfirmationModule.SendButtonState) {
        when(state) {
            SendConfirmationModule.SendButtonState.ACTIVE -> {
                buttonTextView.text = context.getString(R.string.Send_Confirmation_Send_Button)
                progressBar.isVisible = false
                buttonTextView.isEnabled = true
                buttonWrapper.isEnabled = true
            }
            SendConfirmationModule.SendButtonState.SENDING -> {
                buttonTextView.text = context.getString(R.string.Send_Sending)
                progressBar.isVisible = true
                buttonTextView.isEnabled = false
                buttonWrapper.isEnabled = false
            }
        }
        invalidate()
    }

}
