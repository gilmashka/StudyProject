package com.example.studyproject_5.data.repository

import com.example.studyproject_5.data.local.dao.PostDao
import com.example.studyproject_5.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val postDao: PostDao
) {
    suspend fun addPost(
        title: String,
        country: String,
        latitude: Double,
        longitude: Double,
        imagePath: String,
        authorId: Long
    ): Long {
        val post = PostEntity(
            title = title,
            country = country,
            latitude = latitude,
            longitude = longitude,
            imagePath = imagePath,
            authorId = authorId
        )
        return postDao.insert(post)
    }

    fun getAllPosts(): Flow<List<PostEntity>> {
        return postDao.getAll()
    }

    fun getPostsByUser(userId: Long): Flow<List<PostEntity>> {
        return postDao.getByUser(userId)
    }

    fun searchByCountry(query: String): Flow<List<PostEntity>> {
        return postDao.searchByCountry(query)
    }

    fun getNorthernHemisphere(): Flow<List<PostEntity>> {
        return postDao.getNorthernHemisphere()
    }

    fun getSouthernHemisphere(): Flow<List<PostEntity>> {
        return postDao.getSouthernHemisphere()
    }
}