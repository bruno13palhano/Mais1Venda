package com.bruno13palhano.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.bruno13palhano.data.repository.OrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class SyncDataWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val orderRepository: OrderRepository,
) : CoroutineWorker(context, params), Synchronizer {
    override suspend fun doWork(): Result {
        val syncStatus = orderRepository.sync()

        return if (syncStatus) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<SyncDataWork>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .build()
    }
}

interface Synchronizer {
    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}
