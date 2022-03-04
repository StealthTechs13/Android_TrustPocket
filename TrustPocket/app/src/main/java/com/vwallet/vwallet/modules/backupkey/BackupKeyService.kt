package com.vwallet.vwallet.modules.backupkey

import com.vwallet.vwallet.entities.Account
import com.vwallet.vwallet.entities.AccountType
import io.horizontalsystems.core.IPinComponent

class BackupKeyService(
        val account: Account,
        private val pinComponent: IPinComponent
) {
    val words: List<String>
    val passphrase: String
    init {
        if (account.type is AccountType.Mnemonic) {
            words = account.type.words
            passphrase = account.type.passphrase
        } else {
            words = listOf()
            passphrase = ""
        }
    }

    val isPinSet: Boolean
        get() = pinComponent.isPinSet

}
