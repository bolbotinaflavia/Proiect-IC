package com.example.homemanagement.data.database.room

import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.data.database.room.RoomDao

class RoomManager(private val roomDao: RoomDao) {

    suspend fun createRoom(room: Camera) {
        roomDao.insertRoom(room)
    }
    suspend fun getAllRooms(): List<Camera> {
        return roomDao.getAllCameras()
    }

    // Other methods for updating, deleting, etc.
}