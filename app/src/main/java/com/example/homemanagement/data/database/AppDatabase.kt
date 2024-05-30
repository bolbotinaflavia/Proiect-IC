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
import com.example.homemanagement.DateConverter
import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.data.database.room.RoomDao
import com.example.homemanagement.data.database.shoppingItem.ShoppingItem
import com.example.homemanagement.data.database.shoppingItem.ShoppingItemDao

<<<<<<< HEAD
@Database(entities = [Camera::class,Component::class,Element::class,ShoppingItem::class,Task::class], version = 11, exportSchema = false)
@TypeConverters(Converters::class, DateConverter::class)
=======
@Database(entities = [Camera::class,Component::class,Element::class,ShoppingItem::class], version = 9,exportSchema = false)
@TypeConverters(Converters::class)
>>>>>>> d14e45b07bd7b4d1ae8d2d9dfccfa1a6d45588c7
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao():RoomDao
    abstract fun componentDao():ComponentDao
     abstract fun elementDao():ElementDao
    abstract fun shoppingItemDao():ShoppingItemDao
    abstract fun taskDao():TaskDao

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