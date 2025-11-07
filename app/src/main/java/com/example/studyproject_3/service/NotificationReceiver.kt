package com.example.studyproject_3.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.RemoteInput
import androidx.core.app.NotificationManagerCompat
import com.example.studyproject_3.R
import com.example.studyproject_3.ui.screens.UserMessage

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val replyText = remoteInput?.getCharSequence(REPLY_TEXT)?.toString()

        if (!replyText.isNullOrBlank()) {
            val message = UserMessage(replyText)
            MessageRepository.addMessage(message)

            val notificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
            if (notificationId != -1) {
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.cancel(notificationId)
            }

            Toast.makeText(context, context.getString(R.string.reply_saved, replyText), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REPLY_TEXT = "reply_text"
        const val REPLY_ACTION = "com.example.studyproject_3.REPLY_ACTION"
        const val NOTIFICATION_ID = "notification_id"
    }
}