package com.vwallet.vwallet.core.factories

import com.vwallet.vwallet.core.IAccountFactory
import com.vwallet.vwallet.core.IAccountManager
import com.vwallet.vwallet.entities.Account
import com.vwallet.vwallet.entities.AccountOrigin
import com.vwallet.vwallet.entities.AccountType
import java.util.*

class AccountFactory(val accountManager: IAccountManager) : IAccountFactory {

    override fun account(type: AccountType, origin: AccountOrigin, backedUp: Boolean): Account {
        val id = UUID.randomUUID().toString()

        return Account(
                id = id,
                name = getNextWalletName(),
                type = type,
                origin = origin,
                isBackedUp = backedUp
        )
    }


    private fun getNextWalletName(): String {
        val count = accountManager.accounts.count()

        return "Wallet ${count + 1}"
    }
}
