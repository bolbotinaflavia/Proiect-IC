package com.example.homemanagement.data.database.component

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ComponentDao {
    @Query("SELECT * FROM components")
     fun getAllComponents(): List<Component>

    @Insert
    fun insertComponent(component: Component)
}