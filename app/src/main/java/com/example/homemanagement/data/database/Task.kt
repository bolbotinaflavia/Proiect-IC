package com.example.homemanagement.data.database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.homemanagement.data.database.room.Camera

@Entity(tableName = "tasks", foreignKeys = [ForeignKey(entity = Camera::class, parentColumns = ["id"], childColumns = ["roomId"], onDelete = ForeignKey.CASCADE)])
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val date: String?,
    val roomId: Int?,
    //val camera: Camera?,
    val description: String?
)
