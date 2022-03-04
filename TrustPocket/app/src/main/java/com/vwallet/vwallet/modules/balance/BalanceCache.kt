package com.vwallet.vwallet.modules.balance

import com.vwallet.vwallet.core.BalanceData
import com.vwallet.vwallet.core.storage.EnabledWalletsCacheDao
import com.vwallet.vwallet.entities.EnabledWalletCache
import com.vwallet.vwallet.entities.Wallet

class BalanceCache(private val dao: EnabledWalletsCacheDao) {
    private var cacheMap: Map<String, BalanceData>

    init {
        cacheMap = convertToCacheMap(dao.getAll())
    }

    private fun convertToCacheMap(list: List<EnabledWalletCache>): Map<String, BalanceData> {
        return list.map {
            val key = listOf(it.coinId, it.coinSettingsId, it.accountId).joinToString()
            key to BalanceData(it.balance, it.balanceLocked)
        }.toMap()
    }

    fun setCache(wallet: Wallet, balanceData: BalanceData) {
        setCache(mapOf(wallet to balanceData))
    }

    fun getCache(wallet: Wallet): BalanceData? {
        val key = listOf(wallet.coin.id, wallet.configuredCoin.settings.id, wallet.account.id).joinToString()
        return cacheMap[key]
    }

    fun setCache(balancesData: Map<Wallet, BalanceData>) {
        val list = balancesData.map { (wallet, balanceData) ->
            EnabledWalletCache(
                wallet.coin.id,
                wallet.configuredCoin.settings.id,
                wallet.account.id,
                balanceData.available,
                balanceData.locked
            )
        }
        cacheMap = cacheMap + convertToCacheMap(list)

        dao.insertAll(list)
    }

}
