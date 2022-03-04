package com.vwallet.vwallet.core.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vwallet.vwallet.entities.AccountSettingRecord

@Dao
interface AccountSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(record: AccountSettingRecord)

    @Query("SELECT * FROM AccountSettingRecord WHERE accountId = :accountId AND `key` = :key")
    fun get(accountId: String, key: String): AccountSettingRecord?
}
