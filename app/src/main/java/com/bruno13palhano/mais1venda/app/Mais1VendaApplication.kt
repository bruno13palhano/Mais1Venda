package com.bruno13palhano.mais1venda.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.bruno13palhano.data.sync.NewOrdersNotifications
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

private const val CHANNEL_ID = "mais_1_venda_notification_channel"
private const val CHANNEL_NAME = "mais_1_venda_system"

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
        NewOrdersNotifications.initializer(context = this)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
