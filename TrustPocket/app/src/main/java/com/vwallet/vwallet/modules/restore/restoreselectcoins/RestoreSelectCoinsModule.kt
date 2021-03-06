package com.vwallet.vwallet.modules.restore.restoreselectcoins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.entities.AccountType
import com.vwallet.vwallet.modules.blockchainsettings.CoinSettingsViewModel
import com.vwallet.vwallet.modules.enablecoins.*

object RestoreSelectCoinsModule {

    class Factory(private val accountType: AccountType) : ViewModelProvider.Factory {

        private val enableCoinsService by lazy {
            EnableCoinsService(
                    App.buildConfigProvider,
                    App.coinManager,
                    EnableCoinsBep2Provider(App.buildConfigProvider),
                    EnableCoinsEip20Provider(App.networkManager, App.appConfigProvider, EnableCoinsEip20Provider.EnableCoinMode.Erc20),
                    EnableCoinsEip20Provider(App.networkManager, App.appConfigProvider, EnableCoinsEip20Provider.EnableCoinMode.Bep20)
            )
        }

        private val restoreSettingsService by lazy {
            RestoreSettingsService(App.restoreSettingsManager)
        }
        private val coinSettingsService by lazy {
            CoinSettingsService()
        }

        private val restoreSelectCoinsService by lazy {
            RestoreSelectCoinsService(
                    accountType,
                    App.accountFactory,
                    App.accountManager,
                    App.walletManager,
                    App.coinManager,
                    enableCoinsService,
                    restoreSettingsService,
                    coinSettingsService)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                RestoreSettingsViewModel::class.java -> {
                    RestoreSettingsViewModel(restoreSettingsService, listOf(restoreSettingsService)) as T
                }
                CoinSettingsViewModel::class.java -> {
                    CoinSettingsViewModel(coinSettingsService, listOf(coinSettingsService)) as T
                }
                EnableCoinsViewModel::class.java -> {
                    EnableCoinsViewModel(enableCoinsService) as T
                }
                RestoreSelectCoinsViewModel::class.java -> {
                    RestoreSelectCoinsViewModel(restoreSelectCoinsService, listOf(restoreSelectCoinsService)) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}

