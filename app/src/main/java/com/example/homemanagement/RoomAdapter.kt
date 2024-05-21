package com.example.homemanagement

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.homemanagement.R
import com.example.homemanagement.data.database.room.Camera
import java.io.InputStream

class RoomAdapter(private val context: Context, private var rooms: List<Camera>,private val onItemClick: (position: Int) -> Unit) : BaseAdapter() {

    override fun getCount(): Int {
        return rooms.size
    }

    override fun getItem(position: Int): Camera {
        return rooms[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun getItemName(position: Int):String{
        val room = rooms[position]
        return room.name;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.room_item, parent, false)

        val room = rooms[position]

        // Bind room data to views in the grid item layout
        val nameTextView: TextView = view.findViewById(R.id.roomTextView)
        val photoImageView: ImageView = view.findViewById(R.id.roomImageView)

        nameTextView.text = room.name
        // Load room photo using a library like Picasso or Glide
        if (!room.photo.isNullOrEmpty()) {

            try {
                val imageUri = Uri.parse(room.photo)
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                photoImageView.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                photoImageView.setImageResource(R.drawable.home_foreground) // Fallback to default image in case of an error
            }
        } else {
            photoImageView.setImageResource(R.drawable.home)  // Placeholder image
        }

        val btnRoomShow: LinearLayout = view.findViewById(R.id.btn_room_show)
        btnRoomShow.setOnClickListener {
            onItemClick(position)
        }

        return view
    }

    // Method to update the list of rooms in the adapter
    fun updateRooms(newRooms: List<Camera>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}