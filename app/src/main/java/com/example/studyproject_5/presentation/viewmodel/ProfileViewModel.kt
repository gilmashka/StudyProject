package com.example.studyproject_5.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyproject_5.data.local.SessionManager
import com.example.studyproject_5.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId()
            if (userId == 0L) {
                _username.value = "Гость"
            } else {
                val user = userRepository.getUserById(userId)
                _username.value = user?.username ?: "Пользователь"
            }
        }
    }

    fun logout(onResult: (Boolean) -> Unit) {
        sessionManager.clearSession()
        onResult(true)
    }
}