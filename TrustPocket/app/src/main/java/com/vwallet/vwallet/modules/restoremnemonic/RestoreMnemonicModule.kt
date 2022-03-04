package com.vwallet.vwallet.modules.restoremnemonic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.managers.PassphraseValidator

object RestoreMnemonicModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = RestoreMnemonicService(App.wordsManager, PassphraseValidator())

            return RestoreMnemonicViewModel(service, listOf(service)) as T
        }
    }

}
