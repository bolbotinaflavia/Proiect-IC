package com.example.homemanagement.data.database.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.homemanagement.data.database.room.Camera

@Dao
interface RoomDao {
    @Query("SELECT * FROM cameras")
    fun getAllCameras(): List<Camera>

    @Insert
    fun insertRoom(room: Camera)

    @Update
    fun updateRoom(room: Camera)

    @Delete
    fun deleteRoom(room: Camera)

    @Query("SELECT * FROM cameras WHERE id = :id")
    fun getCamera(id: Int): Camera?


}

