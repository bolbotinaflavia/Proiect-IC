package com.example.homemanagement.data.database.component

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.homemanagement.data.database.component.Component
import com.example.homemanagement.data.database.room.Camera


@Dao
interface ComponentDao {
    @Query("SELECT * FROM components")
     fun getAllComponents(): List<Component>

    @Insert
    fun insertComponent(component: Component)

    @Update
    fun updateComponent(component:Component)

    @Delete
    fun deleteComponent(component: Component)

    @Query("SELECT * FROM components WHERE id = :id")
    fun getComponent(id: Int): Component?

    @Query("SELECT * FROM components WHERE roomId = :roomId")
    fun getComponentsByRoom(roomId: Int): List<Component>

    @Query("SELECT * FROM components WHERE userId = :userId")
    fun getComponentsByUser(userId: Int): List<Component>
}