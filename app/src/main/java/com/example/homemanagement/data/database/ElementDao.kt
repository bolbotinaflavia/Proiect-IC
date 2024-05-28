package com.example.homemanagement.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.homemanagement.data.database.Element
import com.example.homemanagement.data.database.room.Camera

@Dao
interface ElementDao {
    @Query("SELECT * FROM elements")
     fun getAllElements(): List<Element>

    @Query("SELECT * FROM elements WHERE componentId = :componentId")
     fun getElementsByComponent(componentId: Int): List<Element>

    @Insert
     fun insertElement(element: Element)

    @Update
     fun updateElement(element: Element)

    @Query("DELETE FROM elements WHERE id = :elementId")
    fun deleteElement(elementId: Int)

}
