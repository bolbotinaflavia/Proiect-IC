package com.example.homemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homemanagement.data.database.Element

class ElementAdapter(
    private val elements: List<Element>,
    private val onEditClick: (Element) -> Unit,
    private val onAddToShopListClick: (Element) -> Unit
) : RecyclerView.Adapter<ElementAdapter.ElementViewHolder>() {

    class ElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val elementNameTextView: TextView = itemView.findViewById(R.id.elementNameTextView)
        val editElementButton: Button = itemView.findViewById(R.id.editElementButton)
        val addToShopListButton: Button = itemView.findViewById(R.id.addToShopListButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_item, parent, false)
        return ElementViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        val element = elements[position]
        holder.elementNameTextView.text = element.name
        holder.editElementButton.setOnClickListener { onEditClick(element) }
        holder.addToShopListButton.setOnClickListener { onAddToShopListClick(element) }
    }

    override fun getItemCount(): Int = elements.size
}
