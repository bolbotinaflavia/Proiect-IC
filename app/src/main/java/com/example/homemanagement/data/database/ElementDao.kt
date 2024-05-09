package com.example.homemanagement.data.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ElementDao {
    @Query("SELECT * FROM elements")
     fun getAllElements(): List<Element>

    // Define other Element-related operations here
}