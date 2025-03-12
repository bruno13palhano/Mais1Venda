package com.bruno13palhano.data.sync

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.bruno13palhano.data.model.shared.Order
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@AndroidEntryPoint
class OrderPollingService : JobService() {
    @Inject
    lateinit var okHttpClient: OkHttpClient
    private var wakeLock: PowerManager.WakeLock? = null
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val ordersListAdapter = moshi.adapter<List<Order>>(
        Types.newParameterizedType(List::class.java, Order::class.java),
    )
    private var job: Job? = null
    private var lastProcessedId: Long = 0
    private val ordersList = mutableListOf<Order>()
    private var isRunning = false

    companion object {
        const val CHANNEL_ID = "ORDERS_CHANNEL"
        const val GROUP_KEY_ORDERS = "com.bruno13palhano.app.ORDERS_GROUP"
        private const val SUMMARY_ID = 0
        const val JOB_ID = 10000
        private const val WAKE_LOK_TAG = "OrderPollingService:WakeLock"
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        acquireWakeLock()
        job = CoroutineScope(Dispatchers.IO).launch {
            checkForNewOrders(params = p0)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        job?.cancel()
        okHttpClient.dispatcher.cancelAll()
        showGroupedNotification()
        releaseWakeLock()
        scheduleNextJob()
        return true
    }

    private suspend fun checkForNewOrders(params: JobParameters?) {
        val endTime = System.currentTimeMillis() + 14 * 60 * 1000
        val url = "http://192.168.1.104:8080/api/orders/pending?lastId=$lastProcessedId"
        var retryDelay = 1000L

        while (System.currentTimeMillis() < endTime && !isRunning) {
            try {
                val request = Request.Builder().url(url = url).build()
                val response = withContext(Dispatchers.IO) {
                    okHttpClient.newCall(request = request).execute()
                }
                if (response.isSuccessful) {
                    val orders = response.body?.string()?.let {
                        ordersListAdapter.fromJson(it)
                    } ?: emptyList()

                    if (orders.isNotEmpty()) {
                        println("New orders received: ${orders.size}")
                        ordersList.addAll(orders)
                        lastProcessedId = orders.maxOf { it.id }
                        showGroupedNotification()
                        retryDelay = 1000L
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                delay(retryDelay)
                retryDelay = (retryDelay * 2).coerceAtMost(30_000L)
            }
        }
        scheduleNextJob()
        jobFinished(params, false)
    }

    private fun showGroupedNotification() {
        if (ordersList.isEmpty()) return

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager
        val intent = Intent("com.bruno13palhano.app.OPEN_MAIN_ACTIVITY").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        ordersList.forEachIndexed { index, order ->
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
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

        val summaryNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("New Orders!")
            .setContentText("${ordersList.size} new orders received")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_ORDERS)
            .setGroupSummary(true)
            .build()
        notificationManager.notify(SUMMARY_ID, summaryNotification)

        ordersList.clear()
    }

    private fun scheduleNextJob() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(this, OrderPollingService::class.java))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setOverrideDeadline(1 * 60 * 1000)
            .build()
        jobScheduler.schedule(jobInfo)
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOK_TAG).apply {
            acquire(15 * 60 * 1000L) // fifteen minutes
        }
    }

    private fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
        wakeLock = null
    }
}
