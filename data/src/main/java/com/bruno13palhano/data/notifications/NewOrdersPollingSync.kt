package com.bruno13palhano.data.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object NewOrdersPollingSync {
    fun initializer(context: Context) {
        val workRequest = NewOrdersPollingWorker.startUpNewOrderNotificationWork()

        WorkManager.getInstance(context).enqueueUniqueWork(
            NewOrdersPollingWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest,
        )
    }
}
