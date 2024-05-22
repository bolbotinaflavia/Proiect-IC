package com.example.homemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.homemanagement.R

class ShoppingItemCreateFragment : Fragment() {

    private var listener: ShoppingItemCreationListener? = null

    interface ShoppingItemCreationListener {
        fun onShoppingItemCreated(name: String)
        fun onShoppingItemCancel()
    }

    fun setShoppingItemCreationListener(listener: ShoppingItemCreationListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_shopping_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemNameEditText: EditText = view.findViewById(R.id.itemNameEditText)
        val addButton: Button = view.findViewById(R.id.addButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        addButton.setOnClickListener {
            val name = itemNameEditText.text.toString()
            listener?.onShoppingItemCreated(name)
        }

        cancelButton.setOnClickListener {
            listener?.onShoppingItemCancel()
        }
    }
}
