package com.example.studyproject_3.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyproject_3.R
import com.example.studyproject_3.service.MessageRepository

data class UserMessage(val text: String)

@Composable
fun UserMessagesScreen() {
    val messages = remember { mutableStateOf(MessageRepository.getAllMessages()) }
    val newMessageText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.value) { message ->
                Text(text = message.text)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newMessageText.value,
                onValueChange = { newMessageText.value = it },
                placeholder = { Text(stringResource(R.string.message_input_hint)) },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (newMessageText.value.isNotBlank()) {
                        val message = UserMessage(newMessageText.value)
                        MessageRepository.addMessage(message)
                        messages.value = MessageRepository.getAllMessages()
                        newMessageText.value = ""
                    }
                }
            ) {
                Text(stringResource(R.string.send_button))
            }
        }
    }
}