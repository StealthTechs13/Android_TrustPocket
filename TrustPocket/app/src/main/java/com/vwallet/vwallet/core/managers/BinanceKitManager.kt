package com.vwallet.vwallet.core.managers

import com.vwallet.vwallet.core.App
import com.vwallet.vwallet.core.IBinanceKitManager
import com.vwallet.vwallet.core.UnsupportedAccountException
import com.vwallet.vwallet.entities.Account
import com.vwallet.vwallet.entities.AccountType
import com.vwallet.vwallet.entities.Wallet
import io.horizontalsystems.binancechainkit.BinanceChainKit

class BinanceKitManager(
        private val testMode: Boolean
) : IBinanceKitManager {
    private var kit: BinanceChainKit? = null
    private var useCount = 0
    private var currentAccount: Account? = null

    override val binanceKit: BinanceChainKit?
        get() = kit

    override val statusInfo: Map<String, Any>?
        get() = kit?.statusInfo()

    override fun binanceKit(wallet: Wallet): BinanceChainKit {
        val account = wallet.account
        val accountType = account.type

        if (kit != null && currentAccount != account) {
            kit?.stop()
            kit = null
            currentAccount = null
        }

        if (kit == null) {
            if (accountType !is AccountType.Mnemonic)
                throw UnsupportedAccountException()

            useCount = 0

            kit = createKitInstance( accountType, account)
            currentAccount = account
        }

        useCount++
        return kit!!
    }

    private fun createKitInstance(accountType: AccountType.Mnemonic, account: Account): BinanceChainKit {
        val networkType = if (testMode)
            BinanceChainKit.NetworkType.TestNet else
            BinanceChainKit.NetworkType.MainNet

        val kit = BinanceChainKit.instance(App.instance, accountType.words, accountType.passphrase, account.id, networkType)
        kit.refresh()

        return kit
    }

    override fun unlink() {
        useCount -= 1

        if (useCount < 1) {
            kit?.stop()
            kit = null
            currentAccount = null
        }
    }

}
