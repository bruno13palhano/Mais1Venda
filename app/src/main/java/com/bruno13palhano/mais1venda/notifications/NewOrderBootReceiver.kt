package com.bruno13palhano.mais1venda.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bruno13palhano.data.notifications.NewOrdersPollingWorker
import java.util.concurrent.TimeUnit

class NewOrderBootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        if (p1.action == Intent.ACTION_BOOT_COMPLETED) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NewOrdersPollingWorker>()
                .setConstraints(constraints)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(p0)
                .enqueueUniqueWork(
                    NewOrdersPollingWorker.WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    workRequest,
                )
        }
    }
}
