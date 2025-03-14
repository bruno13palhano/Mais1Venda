package com.bruno13palhano.data.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

/**
 * Polling for new orders in the background.
 */
object NewOrdersPollingSync {
    /**
     * Starts the polling service.
     *
     * @param context The application context.
     */
    fun initializer(context: Context) {
        val workRequest = NewOrdersPollingWorker.startUpNewOrderNotificationWork()

        WorkManager.getInstance(context).enqueueUniqueWork(
            NewOrdersPollingWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest,
        )
    }
}
