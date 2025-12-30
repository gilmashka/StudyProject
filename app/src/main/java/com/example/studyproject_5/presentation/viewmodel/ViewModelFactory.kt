package com.example.studyproject_5.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studyproject_5.data.local.SessionManager
import com.example.studyproject_5.data.local.database.TravelDatabase
import com.example.studyproject_5.data.repository.FileStorageRepository
import com.example.studyproject_5.data.repository.PostRepository
import com.example.studyproject_5.data.repository.UserRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = TravelDatabase.getDatabase(context)
        val userRepository = UserRepository(database.userDao())
        val postRepository = PostRepository(database.postDao())
        val fileStorageRepository = FileStorageRepository(context)
        val sessionManager = SessionManager(context)

        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(FeedViewModel::class.java) -> {
                FeedViewModel(postRepository) as T
            }
            modelClass.isAssignableFrom(AddPostViewModel::class.java) -> {
                AddPostViewModel(postRepository, fileStorageRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, sessionManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}