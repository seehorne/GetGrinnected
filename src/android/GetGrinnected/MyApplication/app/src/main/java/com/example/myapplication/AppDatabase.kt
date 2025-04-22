package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * This creates our app database as a room database for general accessing
 */
@Database(entities = [EventEntity::class, AccountEntity::class], version = 1)
@TypeConverters(ListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    // This is the pathing for queries
    abstract fun appDao(): AppDao

    // This object makes an initial instance
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // This allows us to get our database for our AppRepository
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
