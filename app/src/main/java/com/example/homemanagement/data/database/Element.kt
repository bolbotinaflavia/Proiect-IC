package com.example.homemanagement.data.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.homemanagement.data.database.component.Component

@Entity(tableName = "elements", foreignKeys = [ForeignKey(entity = Component::class, parentColumns = ["id"], childColumns = ["componentId"], onDelete = ForeignKey.CASCADE)])
data class Element(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var componentId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(componentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }
}
