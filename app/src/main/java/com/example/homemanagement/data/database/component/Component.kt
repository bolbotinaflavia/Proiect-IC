package com.example.homemanagement.data.database.component
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homemanagement.Converters
import com.example.homemanagement.data.database.Element
import com.example.homemanagement.data.database.room.Camera

@Entity(tableName = "components",
    foreignKeys = [ForeignKey(entity = Camera::class,
        parentColumns = ["id"],
        childColumns = ["roomId"], onDelete = ForeignKey.CASCADE)])
data class Component(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var userId:Int?=null,
    var name: String,
    var photo: String? = null,
    val roomId: Int?=null,
    @TypeConverters(Converters::class)
    val elements: List<Element>? = null
)
