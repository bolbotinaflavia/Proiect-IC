package com.example.homemanagement.data.database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.homemanagement.data.database.room.Camera
import java.util.Date

/*@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(entity = Camera::class, parentColumns = ["id"], childColumns = ["roomId"], onDelete = ForeignKey.CASCADE)])*/
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val name: String,
    val date: Date,
    val roomId: Int?=null,
    var isChecked: Boolean = false
)
