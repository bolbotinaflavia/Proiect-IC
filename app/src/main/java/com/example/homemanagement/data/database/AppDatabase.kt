package com.example.homemanagement.data.database

import android.content.Context
import androidx.room.Database
import com.example.homemanagement.data.database.component.Component
import com.example.homemanagement.data.database.component.ComponentDao
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.homemanagement.Converters
import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.data.database.room.RoomDao

@Database(entities = [Camera::class,Component::class,Element::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun componentDao():ComponentDao
     abstract fun elementDao():ElementDao
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