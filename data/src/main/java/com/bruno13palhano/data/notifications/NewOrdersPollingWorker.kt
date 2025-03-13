package com.bruno13palhano.data.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bruno13palhano.data.BuildConfig
import com.bruno13palhano.data.model.shared.Order
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@HiltWorker
class NewOrdersPollingWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val okHttpClient: OkHttpClient,
    moshi: Moshi,
) : CoroutineWorker(appContext = context, params = params) {
    private val ordersListAdapter = moshi.adapter<List<Order>>(
        Types.newParameterizedType(List::class.java, Order::class.java),
    )
    private var lastProcessedId: Long = 0
    private val ordersList = mutableListOf<Order>()

    companion object {
        const val CHANNEL_ID = "NEW_ORDERS_CHANNEL"
        private const val GROUP_KEY_ORDERS = "com.bruno13palhano.mais1venda.ORDERS_GROUP"
        private const val SUMMARY_ID = 0
        const val WORK_NAME = "new_orders_notifications"

        fun startUpNewOrderNotificationWork() = OneTimeWorkRequestBuilder<NewOrdersPollingWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
    }

    override suspend fun doWork(): Result {
        val url = "${BuildConfig.ServerUrl}/api/orders/pending?lastId=$lastProcessedId"
        val endTime = System.currentTimeMillis() + 14 * 60 * 1000 // Execute for fifteen minutes
        var retryDelay = 1000L

        while (System.currentTimeMillis() < endTime && !isStopped) {
            try {
                val request = Request.Builder()
                    .url(url = url)
                    .build()
                val response = withContext(Dispatchers.IO) {
                    okHttpClient.newCall(request = request).execute()
                }

                if (response.isSuccessful) {
                    val orders = response.body?.use { body ->
                        ordersListAdapter.fromJson(body.string()) ?: emptyList()
                    } ?: emptyList()
                    if (orders.isNotEmpty()) {
                        ordersList.addAll(orders)
                        lastProcessedId = orders.maxOf { it.id }
                        showGroupedNotifications()
                        retryDelay = 1000L
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                delay(retryDelay)
                retryDelay = (retryDelay * 2).coerceAtMost(30_000L)
            }
        }

        scheduleNextWork()
        return Result.success()
    }

    private fun showGroupedNotifications() {
        if (ordersList.isEmpty()) return

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager
        val intent = Intent("com.bruno13palhano.mais1venda.OPEN_MAIN_ACTIVITY").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        ordersList.forEachIndexed { index, order ->
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Order!")
                .setContentText("${order.productName} - R$${order.unitPrice}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY_ORDERS)
                .build()
            notificationManager.notify(order.id.toInt(), notification)
        }

        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("New Orders!")
            .setContentText("${ordersList.size} new orders")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_ORDERS)
            .setGroupSummary(true)
            .build()
        notificationManager.notify(SUMMARY_ID, summaryNotification)

        ordersList.clear()
    }

    private fun scheduleNextWork() {
        val nextWork = startUpNewOrderNotificationWork()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.KEEP,
            nextWork,
        )
    }
}
