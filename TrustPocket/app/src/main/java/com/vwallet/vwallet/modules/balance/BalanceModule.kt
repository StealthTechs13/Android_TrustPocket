package com.vwallet.vwallet.modules.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.core.AdapterState
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.BalanceData
import com.vwallet.vwallet.entities.Wallet
import io.horizontalsystems.xrateskit.entities.LatestRate

object BalanceModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val activeAccountService = ActiveAccountService(App.accountManager)

            val balanceService2 = BalanceService2(
                BalanceActiveWalletRepository(App.walletManager, App.accountSettingManager),
                BalanceXRateRepository(App.currencyManager, App.xRateManager),
                BalanceAdapterRepository(App.adapterManager, BalanceCache(App.appDatabase.enabledWalletsCacheDao())),
                NetworkTypeChecker(App.accountSettingManager),
                App.localStorage,
                App.connectivityManager,
                BalanceSorter(),
            )

            val rateAppService = RateAppService(App.rateAppManager)

            return BalanceViewModel(
                balanceService2,
                rateAppService,
                activeAccountService,
                BalanceViewItemFactory(),
                App.appConfigProvider.reportEmail
            ) as T
        }
    }

    data class BalanceItem(
        val wallet: Wallet,
        val mainNet: Boolean,
        val balanceData: BalanceData,
        val state: AdapterState,
        val latestRate: LatestRate? = null
    ) {
        val fiatValue get() = latestRate?.rate?.let { balanceData.available.times(it) }
    }
}
