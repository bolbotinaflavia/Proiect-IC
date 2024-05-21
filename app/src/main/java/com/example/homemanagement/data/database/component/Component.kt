package com.example.homemanagement.data.database.component
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.homemanagement.Converters
import com.example.homemanagement.data.database.Element
import com.example.homemanagement.data.database.room.Camera

//de pus la entity:( ,foreignKeys = [ForeignKey(entity = Camera::class, parentColumns = ["id"], childColumns = ["roomId"], onDelete = ForeignKey.CASCADE)])
@Entity(tableName = "components")
data class Component(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String,
    var photo: String? = null,
    val roomId: Int?=null,
    @TypeConverters(Converters::class)
    val elements: List<Element>? = null
)
