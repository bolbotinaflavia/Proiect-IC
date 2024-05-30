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
import com.example.homemanagement.data.database.component.Component
import java.io.InputStream

class ComponentAdapter (private val context: Context, private var components: List<Component>,private val onItemClick: (position: Int) -> Unit) : BaseAdapter() {

    override fun getCount(): Int {
        return components.size
    }

    override fun getItem(position: Int): Component {
        return components[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.component_item, parent, false)

        val component = components[position]

        // Bind room data to views in the grid item layout
        val nameTextView: TextView = view.findViewById(R.id.componentTextView)
        val photoImageView: ImageView = view.findViewById(R.id.componentImageView)

        nameTextView.text = component.name
        // Load room photo using a library like Picasso or Glide
        if (!component.photo.isNullOrEmpty()) {

            try {
                val imageUri = Uri.parse(component.photo)
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                photoImageView.setImageBitmap(bitmap)
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                photoImageView.setImageResource(R.drawable.home_foreground) // Fallback to default image in case of an error
            }
        } else {
            photoImageView.setImageResource(R.drawable.home_foreground)  // Placeholder image
        }
        val btnComponentShow: LinearLayout = view.findViewById(R.id.btn_component_show)
        btnComponentShow.setOnClickListener {
            onItemClick(position)
        }
        return view
    }

    // Method to update the list of rooms in the adapter
    fun updateComponents(newComponents: List<Component>) {
        components = newComponents
        notifyDataSetChanged()
    }
}