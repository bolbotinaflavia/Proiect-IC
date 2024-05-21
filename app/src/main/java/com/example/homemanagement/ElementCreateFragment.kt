package com.example.homemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class ElementCreateFragment :Fragment() {
    private lateinit var submitButton: Button
    private lateinit var elementNameEditText: EditText
    private lateinit var listener: ElementCreationListener
    private var componentId: Int = 0

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.element_new, container, false)

        componentId=arguments?.getInt("componentId")?:0
        submitButton = view.findViewById(R.id.submitButton_element)
        elementNameEditText = view.findViewById(R.id.elementNameEditText)

        submitButton.setOnClickListener {
            val elementName = elementNameEditText.text.toString()
            listener.onElementCreated(elementName,componentId) // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.cancelButton_element)
        cancelButton.setOnClickListener {
            (requireParentFragment() as? ComponentFragment)?.onComponentCancel()
        }

        return view
    }

    interface ElementCreationListener {
        fun onElementCreated(name: String,componentId:Int)
        fun onElementCancel()
    }
    fun setElementCreationListener(listener: ElementCreationListener) {
        this.listener = listener
    }
}