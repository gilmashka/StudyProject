package com.example.studyproject_5.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyproject_5.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val posts: StateFlow<List<PostItem>> = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadAllPosts()
    }

    fun loadAllPosts() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                postRepository.getAllPosts().collect { postEntities ->
                    _posts.value = postEntities.map { entity ->
                        PostItem(
                            id = entity.id,
                            title = entity.title,
                            country = entity.country,
                            latitude = entity.latitude,
                            longitude = entity.longitude,
                            imagePath = entity.imagePath,
                            authorId = entity.authorId
                        )
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun searchByCountry(query: String) {
        if (query.isBlank()) {
            loadAllPosts()
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                postRepository.searchByCountry(query).collect { postEntities ->
                    _posts.value = postEntities.map { entity ->
                        PostItem(
                            id = entity.id,
                            title = entity.title,
                            country = entity.country,
                            latitude = entity.latitude,
                            longitude = entity.longitude,
                            imagePath = entity.imagePath,
                            authorId = entity.authorId
                        )
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun filterByHemisphere(isNorthern: Boolean) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val flow = if (isNorthern) {
                    postRepository.getNorthernHemisphere()
                } else {
                    postRepository.getSouthernHemisphere()
                }

                flow.collect { postEntities ->
                    _posts.value = postEntities.map { entity ->
                        PostItem(
                            id = entity.id,
                            title = entity.title,
                            country = entity.country,
                            latitude = entity.latitude,
                            longitude = entity.longitude,
                            imagePath = entity.imagePath,
                            authorId = entity.authorId
                        )
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    data class PostItem(
        val id: Long,
        val title: String,
        val country: String,
        val latitude: Double,
        val longitude: Double,
        val imagePath: String,
        val authorId: Long
    )
}