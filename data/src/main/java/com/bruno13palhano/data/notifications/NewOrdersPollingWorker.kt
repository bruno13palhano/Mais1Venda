package com.bruno13palhano.data.notifications

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bruno13palhano.data.BuildConfig
import com.bruno13palhano.data.R
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

/**
 * Worker to poll for new orders in the background.
 */
@HiltWorker
class NewOrdersPollingWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val okHttpClient: OkHttpClient,
    moshi: Moshi,
    private val eventBus: EventBus,
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
        val endTime = System.currentTimeMillis() + 14 * 60 * 1000
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
                        orders.forEach { order ->
                            eventBus.publish(event = OrderEvent.OrderCreated(order = order))
                            showGroupedNotifications()
                        }
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

        val baseUri = "mais1venda://orders/newOrder"
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager

        ordersList.forEachIndexed { index, order ->
            val deepLinkIntent = Intent(Intent.ACTION_VIEW).apply {
                data = "$baseUri/${order.id}".toUri()
                setPackage(context.packageName)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                order.id.toInt(),
                deepLinkIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

            val orderTitle = context.getString(R.string.new_order_notification_title)
            val orderText = context.getString(
                R.string.new_order_notification_text,
                order.productName,
                order.unitPrice,
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(orderTitle)
                .setContentText(orderText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY_ORDERS)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notificationManager.notify(order.id.toInt(), notification)
            }
        }

        val summaryIntent = Intent(Intent.ACTION_VIEW).apply {
            data = "$baseUri/${ordersList[0].id}".toUri()
            setPackage(context.packageName)
        }
        val summaryPendingIntent = PendingIntent.getActivity(
            context,
            SUMMARY_ID,
            summaryIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val ordersTitle = context.getString(R.string.new_orders_notification_title)
        val ordersText = context.getString(R.string.new_orders_notification_text, ordersList.size)

        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(ordersTitle)
            .setContentText(ordersText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(summaryPendingIntent)
            .setGroup(GROUP_KEY_ORDERS)
            .setGroupSummary(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(SUMMARY_ID, summaryNotification)
        }

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
