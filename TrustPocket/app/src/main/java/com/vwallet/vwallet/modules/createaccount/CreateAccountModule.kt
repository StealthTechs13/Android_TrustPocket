package com.vwallet.vwallet.modules.createaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.R
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.managers.PassphraseValidator
import com.vwallet.vwallet.core.providers.Translator

object CreateAccountModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = CreateAccountService(
                    App.accountFactory,
                    App.wordsManager,
                    App.accountManager,
                    App.walletManager,
                    PassphraseValidator(),
                    App.coinKit
            )

            return CreateAccountViewModel(service, listOf(service)) as T
        }
    }

    enum class Kind(val title: String) {
        Mnemonic12(Translator.getString(R.string.CreateWallet_N_Words, 12)),
        Mnemonic24(Translator.getString(R.string.CreateWallet_N_Words, 24)),
    }
}
