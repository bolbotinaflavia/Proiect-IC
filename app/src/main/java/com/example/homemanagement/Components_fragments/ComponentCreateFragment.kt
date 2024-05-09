package com.example.homemanagement.Components_fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.homemanagement.R

class ComponentCreateFragment : Fragment() {
    private lateinit var submitButton: Button
    private lateinit var componentNameEditText: EditText
    private lateinit var listener: ComponentCreationListener // Add listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.new_component, container, false)

        submitButton = view.findViewById(R.id.submitButton_component)
        componentNameEditText = view.findViewById(R.id.componentNameEditText)

        submitButton.setOnClickListener {
            val componentName = componentNameEditText.text.toString()
           listener.onComponentCreated(componentName) // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.cancelButton_component)
        cancelButton.setOnClickListener {
            (requireParentFragment() as? ComponentFragment)?.onComponentCancel()
        }

        return view
    }

    interface ComponentCreationListener {
        fun onComponentCreated(name: String)
        fun onComponentCancel()
    }

    fun setComponentCreationListener(listener: ComponentCreationListener) {
        this.listener = listener
    }
}
