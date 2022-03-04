package com.vwallet.vwallet.core.managers

import com.vwallet.vwallet.core.IAccountCleaner
import com.vwallet.vwallet.core.adapters.*
import com.vwallet.vwallet.core.adapters.zcash.ZcashAdapter

class AccountCleaner(private val testMode: Boolean) : IAccountCleaner {

    override fun clearAccounts(accountIds: List<String>) {
        accountIds.forEach { clearAccount(it) }
    }

    private fun clearAccount(accountId: String) {
        BinanceAdapter.clear(accountId, testMode)
        BitcoinAdapter.clear(accountId, testMode)
        BitcoinCashAdapter.clear(accountId, testMode)
        DashAdapter.clear(accountId, testMode)
        EvmAdapter.clear(accountId, testMode)
        Eip20Adapter.clear(accountId, testMode)
        ZcashAdapter.clear(accountId, testMode)
    }

}
