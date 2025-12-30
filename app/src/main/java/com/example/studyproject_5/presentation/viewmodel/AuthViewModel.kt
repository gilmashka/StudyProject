package com.example.studyproject_5.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyproject_5.R
import com.example.studyproject_5.data.local.SessionManager
import com.example.studyproject_5.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    fun register(username: String, password: String, onResult: (Boolean, Int?) -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            onResult(false, R.string.error_field_required)
            return
        }

        viewModelScope.launch {
            try {
                val userId = userRepository.register(username, password)
                sessionManager.saveUserId(userId)
                onResult(true, R.string.success_registration)
            } catch (e: Exception) {
                if (e.message == "Username already exists") {
                    onResult(false, R.string.error_user_exists)
                } else {
                    onResult(false, R.string.unexpected_error)
                }
            }
        }
    }

    fun login(username: String, password: String, onResult: (Boolean, Int?) -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            onResult(false, R.string.error_field_required)
            return
        }

        viewModelScope.launch {
            try {
                val user = userRepository.login(username, password)
                if (user != null) {
                    sessionManager.saveUserId(user.id)
                    onResult(true, R.string.success_login)
                } else {
                    onResult(false, R.string.error_invalid_credentials)
                }
            } catch (e: Exception) {
                onResult(false, R.string.unexpected_error)
            }
        }
    }
}