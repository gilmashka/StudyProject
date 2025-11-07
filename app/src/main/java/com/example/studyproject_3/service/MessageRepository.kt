package com.example.studyproject_3.service

import com.example.studyproject_3.ui.screens.UserMessage

object MessageRepository {
    private val messages = mutableListOf<UserMessage>()

    fun addMessage(message: UserMessage) {
        messages.add(message)
    }

    fun getAllMessages(): List<UserMessage> {
        return messages.toList()
    }

    fun clearMessages() {
        messages.clear()
    }
}