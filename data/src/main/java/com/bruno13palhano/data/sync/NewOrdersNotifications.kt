package com.bruno13palhano.data.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

object NewOrdersNotifications {
    fun initializer(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(
                NEW_ORDERS_NOTIFICATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                OrderNotificationWorker.Companion.startUpNewOrderNotificationWork(),
            )
        }
    }
}

internal const val NEW_ORDERS_NOTIFICATION_WORK_NAME = "NewOrdersNotificationWorker"
