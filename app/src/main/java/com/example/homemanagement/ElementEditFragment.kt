package com.example.homemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.homemanagement.data.database.Element

class ElementEditFragment : Fragment() {
    private lateinit var editButton: Button
    private lateinit var elementNameEditText: EditText
    private lateinit var listener: ElementEditListener
    private lateinit var element: Element

    companion object {
        private const val ARG_ELEMENT = "element"

        fun newInstance(element: Element): ElementEditFragment {
            val fragment = ElementEditFragment()
            val args = Bundle()
            args.putParcelable(ARG_ELEMENT, element)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.element_edit, container, false)
        editButton = view.findViewById(R.id.editelementButton)
        elementNameEditText = view.findViewById(R.id.elementNameEditText)

        element = arguments?.getParcelable(ARG_ELEMENT) ?: throw IllegalStateException("Element required")

        elementNameEditText.setText(element.name)

        editButton.setOnClickListener {
            val elementName = elementNameEditText.text.toString()
            listener.onElementEdit(element, elementName)
        }

        val cancelButton: Button = view.findViewById(R.id.cancelelementButton)
        cancelButton.setOnClickListener {
            listener.onElementCancel()
        }

        val deleteButton: Button = view.findViewById(R.id.deleteelementButton)
        deleteButton.setOnClickListener {
            listener.onElementDelete(element)
        }

        return view
    }

    interface ElementEditListener {
        fun onElementEdit(element: Element, name: String)
        fun onElementCancel()
        fun onElementDelete(element: Element)
    }

    fun setElementEditListener(listener: ElementEditListener) {
        this.listener = listener
    }
}
