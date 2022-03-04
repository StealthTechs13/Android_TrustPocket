package com.vwallet.vwallet.modules.balance

import com.vwallet.vwallet.core.Clearable
import com.vwallet.vwallet.core.IRateAppManager

class RateAppService(private val rateAppManager: IRateAppManager) : Clearable {

    fun onBalancePageActive() {
        rateAppManager.onBalancePageActive()
    }

    fun onBalancePageInactive() {
        rateAppManager.onBalancePageInactive()
    }

    override fun clear() = Unit
}
