package com.example.studyproject_5.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studyproject_5.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(post: PostEntity): Long

    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: Long): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE country LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchByCountry(query: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE latitude > 0 ORDER BY createdAt DESC")
    fun getNorthernHemisphere(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE latitude < 0 ORDER BY createdAt DESC")
    fun getSouthernHemisphere(): Flow<List<PostEntity>>
}