package com.vwallet.vwallet.core.notifications

import com.google.gson.JsonObject
import com.vwallet.vwallet.core.IAppConfigProvider
import com.vwallet.vwallet.core.ILocalStorage
import com.vwallet.vwallet.core.INetworkManager
import com.vwallet.vwallet.entities.SubscriptionJob
import retrofit2.Response
import java.util.*

class NotificationNetworkWrapper(
        private val localStorage: ILocalStorage,
        private val networkManager: INetworkManager,
        appConfigProvider: IAppConfigProvider
) {

    private val host = appConfigProvider.notificationUrl

    private val notificationId: String
        get() {
            var notificationId = localStorage.notificationId
            if (notificationId == null) {
                notificationId = UUID.randomUUID().toString()
                localStorage.notificationId = notificationId
            }
            return notificationId
        }

    suspend fun processSubscription(jobType: SubscriptionJob.JobType, body: String) {
        when (jobType) {
            SubscriptionJob.JobType.Subscribe -> networkManager.subscribe(host, "subscribe/$notificationId", body)
            SubscriptionJob.JobType.Unsubscribe -> networkManager.unsubscribe(host, "unsubscribe/$notificationId", body)
        }
    }

    suspend fun fetchNotifications(): Response<JsonObject> {
        return networkManager.getNotifications(host, "messages/$notificationId")
    }

}
