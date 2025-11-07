package com.example.studyproject_3.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.studyproject_3.MainActivity
import com.example.studyproject_3.R
import com.example.studyproject_3.ui.screens.ReplyHandlerActivity

enum class NotificationPriority {
    HIGH, MEDIUM, LOW, MIN
}

class NotificationService(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var currentId = 1

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun generateNotificationId(): Int {
        return currentId++.also {
            if (it > 99) currentId = 1
        }
    }

    fun createBasicNotification(
        title: String,
        message: String,
        notificationId: Int
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun createAdvancedNotification(
        title: String,
        message: String,
        notificationId: Int,
        expandable: Boolean = false,
        priority: NotificationPriority = NotificationPriority.MEDIUM,
        openAppOnClick: Boolean = false,
        addReplyAction: Boolean = false
    ) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.priority = getAndroidPriority(priority)
        }

        if (expandable && message.isNotBlank()) {
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        }

        if (openAppOnClick) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(context.getString(R.string.notification_title_key), title)
                putExtra(context.getString(R.string.notification_message_key), message)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.setContentIntent(pendingIntent)
        }

        if (addReplyAction) {
            val replyIntent = Intent(context, ReplyHandlerActivity::class.java).apply {
                putExtra(context.getString(R.string.notification_id), notificationId)
            }

            val replyPendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val replyAction = NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_send,
                context.getString(R.string.reply_button_text),
                replyPendingIntent
            ).addRemoteInput(
                androidx.core.app.RemoteInput.Builder(context.getString(R.string.reply_text))
                    .setLabel(context.getString(R.string.reply_input_hint))
                    .build()
            ).build()

            notificationBuilder.addAction(replyAction)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun isNotificationActive(notificationId: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNotifications = notificationManager.activeNotifications
            return activeNotifications.any { it.id == notificationId }
        } else {
            return true
        }
    }

    fun updateNotificationIfExists(
        notificationId: Int,
        title: String,
        message: String
    ): Boolean {
        return if (isNotificationActive(notificationId)) {
            createBasicNotification(title, message, notificationId)
            true
        } else {
            false
        }
    }

    private fun getAndroidPriority(priority: NotificationPriority): Int {
        return when (priority) {
            NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationPriority.MEDIUM -> NotificationCompat.PRIORITY_DEFAULT
            NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
            NotificationPriority.MIN -> NotificationCompat.PRIORITY_MIN
        }
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    companion object {
        const val CHANNEL_ID = "study_project_channel"
    }
}