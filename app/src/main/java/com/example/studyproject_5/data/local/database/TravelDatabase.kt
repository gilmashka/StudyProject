package com.example.studyproject_5.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.studyproject_5.data.local.dao.PostDao
import com.example.studyproject_5.data.local.dao.UserDao
import com.example.studyproject_5.data.local.entity.PostEntity
import com.example.studyproject_5.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}