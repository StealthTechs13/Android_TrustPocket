package com.vwallet.vwallet.modules.balance

import com.vwallet.vwallet.core.IWalletManager
import com.vwallet.vwallet.core.managers.AccountSettingManager
import com.vwallet.vwallet.entities.Wallet
import io.reactivex.Observable

class BalanceActiveWalletRepository(
    private val walletManager: IWalletManager,
    private val accountSettingManager: AccountSettingManager
) {

    val itemsObservable: Observable<List<Wallet>> =
        Observable
            .merge(
                Observable.just(Unit),
                walletManager.activeWalletsUpdatedObservable,
                accountSettingManager.ethereumNetworkObservable,
                accountSettingManager.binanceSmartChainNetworkObservable
            )
            .map {
                walletManager.activeWallets
            }

    fun disable(wallet: Wallet) {
        walletManager.delete(listOf(wallet))
    }

    fun enable(wallet: Wallet) {
        walletManager.save(listOf(wallet))
    }

}
