package com.example.homemanagement.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.homemanagement.data.database.Task
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
     fun getAllTasks(): List<Task>

    // Define other Task-related operations here
}