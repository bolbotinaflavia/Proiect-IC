package com.example.homemanagement.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.homemanagement.data.database.Task
import com.example.homemanagement.data.database.component.Component

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<Task>

     @Insert
     fun insertTask(task:Task)
    @Update
    fun updateTask(task: Task)
     @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id: Int): Task?

    @Query("SELECT * FROM tasks WHERE roomId = :roomId")
    fun getTasksByRoom(roomId: Int): List<Task>

}