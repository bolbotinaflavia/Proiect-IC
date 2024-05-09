package com.example.homemanagement.data.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.homemanagement.data.database.room.Camera

@Dao
interface RoomDao {
    @Query("SELECT * FROM cameras")
    fun getAllCameras(): List<Camera>

    @Insert
    fun insertRoom(room: Camera)
}

