package com.example.homemanagement.data.database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.homemanagement.data.database.component.Component

@Entity(tableName = "elements", foreignKeys = [ForeignKey(entity = Component::class, parentColumns = ["id"], childColumns = ["componentId"], onDelete = ForeignKey.CASCADE)])
data class Element(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val componentId: Int? // Foreign key to Component
)
