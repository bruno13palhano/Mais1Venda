package com.bruno13palhano.mais1venda.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.bruno13palhano.data.notifications.NewOrdersPollingSync
import com.bruno13palhano.data.notifications.NewOrdersPollingWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Mais1VendaApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory = workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        NewOrdersPollingSync.initializer(context = this)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NewOrdersPollingWorker.CHANNEL_ID,
            "New Orders Notifications",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Notifications for new orders"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
