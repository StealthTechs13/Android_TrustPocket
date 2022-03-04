package com.vwallet.vwallet.modules.balanceonboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.core.App

object BalanceOnboardingModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BalanceOnboardingViewModel(App.accountManager, App.walletManager) as T
        }
    }
}
