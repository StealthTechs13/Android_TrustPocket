package com.vwallet.vwallet.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vwallet.vwallet.core.notifications.NotificationWorker

class BootCompletionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent?) {
        NotificationWorker.startPeriodicWorker(context)
    }
}
