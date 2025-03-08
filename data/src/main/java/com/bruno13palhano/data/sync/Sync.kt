package com.bruno13palhano.data.sync

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object Sync {
    fun initializer(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                SyncDataWork.startUpSyncWork()
            )
        }
    }
}

internal const val SYNC_WORK_NAME = "SyncDatabaseWorker"
