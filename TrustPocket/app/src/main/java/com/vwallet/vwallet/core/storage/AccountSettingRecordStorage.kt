package com.vwallet.vwallet.core.storage

import com.vwallet.vwallet.entities.AccountSettingRecord

class AccountSettingRecordStorage(appDatabase: AppDatabase) {
    val dao = appDatabase.accountSettingDao()

    fun accountSetting(accountId: String, key: String): AccountSettingRecord? {
        return dao.get(accountId, key)
    }

    fun save(accountSetting: AccountSettingRecord) {
        dao.save(accountSetting)
    }

    fun deleteAllAccountSettings(accountId: String) {
        TODO()
    }

}
