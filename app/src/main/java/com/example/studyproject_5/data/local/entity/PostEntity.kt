package com.example.studyproject_5.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["authorId"])]
)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val imagePath: String,
    val authorId: Long,
    val createdAt: Long = System.currentTimeMillis()
)