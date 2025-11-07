package com.example.studyproject_3.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.RemoteInput
import androidx.core.app.NotificationManagerCompat
import com.example.studyproject_3.R
import com.example.studyproject_3.service.MessageRepository

class ReplyHandlerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleReply(intent)
        finish()
    }

    private fun handleReply(intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val replyText = remoteInput?.getCharSequence(getString(R.string.reply_text))?.toString()

        if (!replyText.isNullOrBlank()) {
            val message = UserMessage(replyText)
            MessageRepository.addMessage(message)

            val notificationId = intent.getIntExtra(getString(R.string.notification_id), -1)
            if (notificationId != -1) {
                val notificationManager = NotificationManagerCompat.from(this)
                notificationManager.cancel(notificationId)
            }

            Toast.makeText(
                this,
                getString(R.string.reply_saved, replyText),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}