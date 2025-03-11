package com.bruno13palhano.mais1venda.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bruno13palhano.data.sync.OrderNotificationWorker
import java.util.concurrent.TimeUnit

class NewOrderBootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        if (p1.action == Intent.ACTION_BOOT_COMPLETED) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val oneTimeRequest = OneTimeWorkRequestBuilder<OrderNotificationWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(p0).enqueue(oneTimeRequest)

            val periodicRequest = PeriodicWorkRequestBuilder<OrderNotificationWorker>(
                15,
                TimeUnit.MINUTES,
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(p0)
                .enqueueUniquePeriodicWork(
                    NEW_ORDERS_NOTIFICATION_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicRequest,
                )
        }
    }
}

internal const val NEW_ORDERS_NOTIFICATION_WORK_NAME = "NewOrdersNotificationWorker"
