package com.vwallet.vwallet.core.storage

import androidx.room.*
import com.vwallet.vwallet.entities.SubscriptionJob

@Dao
interface SubscriptionJobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(subscriptionJob: SubscriptionJob)

    @Query("SELECT * FROM SubscriptionJob")
    fun all(): List<SubscriptionJob>

    @Delete()
    fun delete(subscriptionJob: SubscriptionJob)

}
