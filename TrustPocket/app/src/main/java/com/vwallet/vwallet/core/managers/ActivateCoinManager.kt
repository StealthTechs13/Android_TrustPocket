package com.vwallet.vwallet.core.managers

import com.vwallet.vwallet.core.IAccountManager
import com.vwallet.vwallet.core.IWalletManager
import com.vwallet.vwallet.entities.Wallet
import io.horizontalsystems.coinkit.CoinKit
import io.horizontalsystems.coinkit.models.CoinType

class ActivateCoinManager(
        private val coinKit: CoinKit,
        private val walletManager: IWalletManager,
        private val accountManager: IAccountManager
) {

    fun activate(coinType: CoinType) {
        val coin = coinKit.getCoin(coinType) ?: return // coin type is not supported

        if (walletManager.activeWallets.any { it.coin == coin })  return // wallet already exists

        val account = accountManager.activeAccount ?: return // active account does not exist

        val wallet = Wallet(coin, account)
        walletManager.save(listOf(wallet))
    }

}
