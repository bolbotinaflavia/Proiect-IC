package com.example.homemanagement.Components_fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.homemanagement.R
import com.example.homemanagement.data.database.component.Component

class ComponentAdapter (private val context: Context, private var components: List<Component>) : BaseAdapter() {

    override fun getCount(): Int {
        return components.size
    }

    override fun getItem(position: Int): Any {
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
        Glide.with(context).load(component.photo).into(photoImageView)

        return view
    }

    // Method to update the list of rooms in the adapter
    fun updateComponents(newComponents: List<Component>) {
        components = newComponents
        notifyDataSetChanged()
    }
}