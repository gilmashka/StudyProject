package com.example.studyproject_3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.studyproject_3.R
import com.example.studyproject_3.service.NotificationService
import com.example.studyproject_3.service.NotificationPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen() {
    val context = LocalContext.current
    val notificationService = remember { NotificationService(context) }
    val notificationTitle = remember { mutableStateOf("") }
    val notificationMessage = remember { mutableStateOf("") }
    val expandNotification = remember { mutableStateOf(false) }
    val openAppOnClick = remember { mutableStateOf(false) }
    val addReplyAction = remember { mutableStateOf(false) }
    val selectedPriority = remember { mutableStateOf(NotificationPriority.MEDIUM) }
    val priorityExpanded = remember { mutableStateOf(false) }
    val lastCreatedNotificationId = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(notificationMessage.value) {
        if (notificationMessage.value.isBlank()) {
            expandNotification.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.notification_settings_title))

        OutlinedTextField(
            value = notificationTitle.value,
            onValueChange = { notificationTitle.value = it },
            placeholder = { Text(stringResource(R.string.notification_title_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notificationMessage.value,
            onValueChange = { notificationMessage.value = it },
            placeholder = { Text(stringResource(R.string.notification_message_hint)) },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = priorityExpanded.value,
            onExpandedChange = { priorityExpanded.value = !priorityExpanded.value }
        ) {
            OutlinedTextField(
                value = getPriorityText(selectedPriority.value),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.priority_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded.value) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = priorityExpanded.value,
                onDismissRequest = { priorityExpanded.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.priority_high)) },
                    onClick = {
                        selectedPriority.value = NotificationPriority.HIGH
                        priorityExpanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.priority_medium)) },
                    onClick = {
                        selectedPriority.value = NotificationPriority.MEDIUM
                        priorityExpanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.priority_low)) },
                    onClick = {
                        selectedPriority.value = NotificationPriority.LOW
                        priorityExpanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.priority_min)) },
                    onClick = {
                        selectedPriority.value = NotificationPriority.MIN
                        priorityExpanded.value = false
                    }
                )
            }
        }

        Row {
            Text(
                text = stringResource(R.string.expand_notification_switch),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = expandNotification.value,
                onCheckedChange = { expandNotification.value = it },
                enabled = notificationMessage.value.isNotBlank()
            )
        }

        Row {
            Text(
                text = stringResource(R.string.open_app_switch),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = openAppOnClick.value,
                onCheckedChange = { openAppOnClick.value = it }
            )
        }

        Row {
            Text(
                text = stringResource(R.string.add_reply_switch),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = addReplyAction.value,
                onCheckedChange = { addReplyAction.value = it }
            )
        }

        if (lastCreatedNotificationId.value != null) {
            Text(stringResource(R.string.notification_id_display, lastCreatedNotificationId.value ?: 0))
        }

        Button(
            onClick = {
                if (notificationTitle.value.isBlank()) {
                    Toast.makeText(context, R.string.title_error, Toast.LENGTH_SHORT).show()
                } else {
                    val notificationId = notificationService.generateNotificationId()
                    lastCreatedNotificationId.value = notificationId

                    notificationService.createAdvancedNotification(
                        title = notificationTitle.value,
                        message = notificationMessage.value,
                        notificationId = notificationId,
                        expandable = expandNotification.value,
                        priority = selectedPriority.value,
                        openAppOnClick = openAppOnClick.value,
                        addReplyAction = addReplyAction.value
                    )

                    Toast.makeText(context, context.getString(R.string.notification_created, notificationId), Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.create_notification_button))
        }
    }
}

@Composable
private fun getPriorityText(priority: NotificationPriority): String {
    return when (priority) {
        NotificationPriority.HIGH -> stringResource(R.string.priority_high)
        NotificationPriority.MEDIUM -> stringResource(R.string.priority_medium)
        NotificationPriority.LOW -> stringResource(R.string.priority_low)
        NotificationPriority.MIN -> stringResource(R.string.priority_min)
    }
}