package com.bruno13palhano.mais1venda.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.bruno13palhano.mais1venda.MainActivity
import com.bruno13palhano.mais1venda.R

private const val PRIMARY_CHANNEL_ID = "mais_1_venda_channel"

internal class NewOrderReceiver : BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManager

    override fun onReceive(p0: Context?, p1: Intent?) {
        notificationManager = p0?.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager

        setNotification(context = p0, extras = p1?.extras)
    }

    private fun setNotification(context: Context, extras: Bundle?) {
        val notificationId = extras?.getInt("id") ?: 0
        val title = extras?.getString("title") ?: ""
        val description = extras?.getString("description") ?: ""

        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )

        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(notificationId, builder.build())
    }
}
