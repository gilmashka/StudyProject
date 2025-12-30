package com.example.studyproject_5.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.studyproject_5.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun authenticate(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getByUsername(username: String): UserEntity?


    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun checkUsernameExists(username: String): Int

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getById(userId: Long): UserEntity?
}