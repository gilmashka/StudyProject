package com.example.studyproject_5.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyproject_5.R
import com.example.studyproject_5.data.local.SessionManager
import com.example.studyproject_5.data.repository.FileStorageRepository
import com.example.studyproject_5.data.repository.PostRepository
import kotlinx.coroutines.launch

class AddPostViewModel(
    private val postRepository: PostRepository,
    private val fileStorageRepository: FileStorageRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        return fileStorageRepository.bitmapFromUri(uri)
    }

    fun addPost(
        title: String,
        country: String,
        latitude: String,
        longitude: String,
        imageBitmap: Bitmap?,
        onResult: (Boolean, Int?) -> Unit
    ) {
        if (title.isBlank()) {
            onResult(false, R.string.error_title_required)
            return
        }
        if (country.isBlank()) {
            onResult(false, R.string.error_country_required)
            return
        }
        if (imageBitmap == null) {
            onResult(false, R.string.error_photo_required)
            return
        }

        val lat = latitude.toDoubleOrNull()
        val lon = longitude.toDoubleOrNull()

        if (lat == null) {
            onResult(false, R.string.error_invalid_latitude)
            return
        }
        if (lon == null) {
            onResult(false, R.string.error_invalid_longitude)
            return
        }
        if (lat > 90 || lat < -90) {
            onResult(false, R.string.error_latitude_out_of_range)
            return
        }
        if (lon > 180 || lon < -180) {
            onResult(false, R.string.error_longitude_out_of_range)
            return
        }

        val authorId = sessionManager.getUserId()
        if (authorId == 0L) {
            onResult(false, R.string.error_not_logged_in)
            return
        }

        viewModelScope.launch {
            try {
                val imagePath = fileStorageRepository.saveImage(imageBitmap)
                val postId = postRepository.addPost(
                    title = title,
                    country = country,
                    latitude = lat,
                    longitude = lon,
                    imagePath = imagePath,
                    authorId = authorId
                )
                onResult(true, R.string.success_post_added)
            } catch (e: Exception) {
                onResult(false, R.string.unexpected_error)
            }
        }
    }
}