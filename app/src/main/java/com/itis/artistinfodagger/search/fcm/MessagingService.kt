package com.itis.artistinfodagger.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.itis.artistinfodagger.MainActivity

class MessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val kind = data["kind"] ?: "default"
        val title = data["title"] ?: "MusicApp"
        val body = data["message"] ?: ""

        when (kind) {
            "promo" -> showNotification(title, body, "promo_channel", "Акции")
            "auth" -> showNotification(title, body, "auth_channel", "Безопасность")
            else -> showNotification(title, body, "default_channel", "Уведомления")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun showNotification(title: String, body: String, channelId: String, channelName: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // стандартная иконка
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationId = when (channelId) {
            "promo_channel" -> 1
            "auth_channel" -> 2
            else -> 3
        }
        notificationManager.notify(notificationId, notification)
    }
}