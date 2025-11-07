package com.example.studyproject_3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.example.studyproject_3.R
import com.example.studyproject_3.service.NotificationService

@Composable
fun EditNotificationScreen() {
    val context = LocalContext.current
    val notificationService = remember { NotificationService(context) }
    val notificationId = remember { mutableStateOf("") }
    val notificationText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.edit_notification_title))

        OutlinedTextField(
            value = notificationId.value,
            onValueChange = { notificationId.value = it },
            placeholder = { Text(stringResource(R.string.notification_id_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notificationText.value,
            onValueChange = { notificationText.value = it },
            placeholder = { Text(stringResource(R.string.notification_text_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    if (notificationId.value.isNotBlank() && notificationText.value.isNotBlank()) {
                        val id = notificationId.value.toIntOrNull()
                        if (id != null) {
                            val updated = notificationService.updateNotificationIfExists(
                                id,
                                context.getString(R.string.updated_notification_title),
                                notificationText.value
                            )
                            if (updated) {
                                Toast.makeText(context, R.string.update_success, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, R.string.update_error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, R.string.update_error, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.update_button))
            }

            Button(
                onClick = {
                    notificationService.cancelAllNotifications()
                    Toast.makeText(context, R.string.cancel_success, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel_all_button))
            }
        }
    }
}