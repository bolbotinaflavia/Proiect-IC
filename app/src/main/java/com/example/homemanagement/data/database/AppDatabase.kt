package com.example.homemanagement.data.database

import android.content.Context
import androidx.room.Database
import com.example.homemanagement.data.database.component.Component
import com.example.homemanagement.data.database.component.ComponentDao
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.homemanagement.Converters
import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.data.database.room.RoomDao

@Database(entities = [Camera::class,Component::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun componentDao():ComponentDao
//    abstract fun ElementDao():ElementDao
//    abstract fun TaskDao():TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        suspend fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }

}