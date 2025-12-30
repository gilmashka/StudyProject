package com.example.studyproject_5.data.repository

import com.example.studyproject_5.data.local.dao.UserDao
import com.example.studyproject_5.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun register(username: String, password: String): Long {
        val existing = userDao.getByUsername(username)
        if (existing != null) {
            throw Exception("Username already exists")
        }

        val user = UserEntity(
            username = username,
            password = password
        )
        return userDao.insert(user)
    }

    suspend fun login(username: String, password: String): UserEntity? {
        return userDao.authenticate(username, password)
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getByUsername(username)
    }

    suspend fun getUserById(userId: Long): UserEntity? {
        return userDao.getById(userId)
    }
}