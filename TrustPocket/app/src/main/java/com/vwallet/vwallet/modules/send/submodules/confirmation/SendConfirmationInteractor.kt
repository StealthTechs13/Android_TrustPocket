package com.vwallet.vwallet.modules.send.submodules.confirmation

import com.vwallet.vwallet.core.IClipboardManager


class SendConfirmationInteractor(private val clipboardManager: IClipboardManager)
    : SendConfirmationModule.IInteractor {

    var delegate: SendConfirmationModule.IInteractorDelegate? = null

    override fun copyToClipboard(coinAddress: String) {
        clipboardManager.copyText(coinAddress)
        delegate?.didCopyToClipboard()
    }

}
