package com.bruno13palhano.mais1venda.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bruno13palhano.data.datasource.remote.WebSocketListener
import com.bruno13palhano.data.datasource.remote.WebSocketManager

private const val CHANNEL_ID = "mais_1_venda_notification_channel"
private const val CHANNEL_NAME = "mais_1_venda_system"
private const val CHANNEL_DESCRIPTION = "Channel to notifies new orders"

class WebSocketService : Service(), WebSocketListener {
    //extract to constructor
    private lateinit var webSocketManager: WebSocketManager

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        webSocketManager = WebSocketManager(webSocketListener = this)
        startForeground(1, createForegroundNotification())
        webSocketManager.connect()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onConnected() {
        println("Service connected to WebSocket")
    }

    override fun onMessage(message: String) {
        if (message.contains("newOrder")) {
            showNotification(title = "New Order", message = "Received a new order")
        }
    }

    override fun onError(error: String) {
        println("Service error: $error")
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = CHANNEL_DESCRIPTION
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("App is running in background")
            .setContentText("Listening for new orders...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        notificationManager.notify(2, builder.build())
    }

    override fun onDestroy() {
        webSocketManager.disconnect()
        super.onDestroy()
    }
}
