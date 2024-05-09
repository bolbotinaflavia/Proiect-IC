package com.example.homemanagement.data.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homemanagement.Converters
import com.example.homemanagement.data.database.component.Component
import com.example.homemanagement.data.database.Task

@Entity(tableName = "cameras")
data class Camera(
    @PrimaryKey(autoGenerate = true) val id: Int? = null, // Nullable id
    var name: String,
    var photo: String? = null,
    var description: String?,
    @TypeConverters(Converters::class)
    val tasks: MutableList<Task>? = mutableListOf(),
    @TypeConverters(Converters::class)
    var components: MutableList<Component>? = mutableListOf()
)

