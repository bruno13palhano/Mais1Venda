package com.bruno13palhano.data.sync

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.bruno13palhano.data.datasource.remote.WebSocketClientInterface
import com.bruno13palhano.data.model.shared.Order
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.delay

private const val CHANNEL_ID = "mais_1_venda_notification_channel"

@HiltWorker
class OrderNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val webSocketClient: WebSocketClientInterface,
) : CoroutineWorker(appContext = appContext, params = params) {

    override suspend fun doWork(): Result {
        return try {
            setUpWebSocket()

            val endTime = System.currentTimeMillis() + 14 * 60 * 1000
            while (System.currentTimeMillis() < endTime && !isStopped) {
                delay(1000)
            }

            webSocketClient.disconnect()
            Result.success()
        } catch (e: CancellationException) {
            webSocketClient.disconnect()
            Result.failure()
        }
    }

    companion object {
        fun startUpNewOrderNotificationWork() = PeriodicWorkRequestBuilder<OrderNotificationWorker>(
            15,
            TimeUnit.MINUTES,
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .build()
    }

    private fun setUpWebSocket() {
        webSocketClient.setOnMessageListener { message ->
            try {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val order = moshi.adapter(Order::class.java).fromJson(message)
                order?.let { showNotification(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        webSocketClient.connect()
    }

    private fun showNotification(order: Order) {
        val notificationManager = appContext.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager

        val intent = Intent("com.bruno13palhano.app.OPEN_MAIN_ACTIVITY").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("New Order!")
            .setContentText("${order.productName} - R$${order.unitPrice}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(order.id.toInt(), notification)
    }
}
