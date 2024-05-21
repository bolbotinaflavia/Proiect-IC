package com.example.homemanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class ComponentEditFragment:Fragment() {
    private lateinit var editButton: Button
    private lateinit var componentNameEditText: EditText
    private lateinit var selectPhotoButton: Button
    private lateinit var listener: ComponentEditListener // Add listener

    companion object {
        private const val ARG_COMPONENT_NAME = "component_name"
        private const val ARG_COMPONENT_PHOTO="component_photo"

        fun newInstance(componentName: String,componentPhoto:String): ComponentEditFragment {
            val fragment = ComponentEditFragment()
            val args = Bundle()
            args.putString(ARG_COMPONENT_NAME, componentName)
            args.putString(ARG_COMPONENT_PHOTO,componentPhoto)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.component_edit, container, false)

        editButton = view.findViewById(R.id.editcomponentButton)
        componentNameEditText = view.findViewById(R.id.componentNameEditText)
        selectPhotoButton = view.findViewById(R.id.selectcomponentPhotoButton)

        selectPhotoButton.setOnClickListener {
            openImageChooser()
        }

        // Set initial values from arguments
        val initialcomponentName = arguments?.getString(ARG_COMPONENT_NAME) ?: ""
        val initialcomponentPhoto=arguments?.getString(ARG_COMPONENT_PHOTO)?:""
        componentNameEditText.setText(initialcomponentName)
        selectPhotoButton.setText(initialcomponentPhoto)

        editButton.setOnClickListener {
            val componentName = componentNameEditText.text.toString()
            val componentPhoto=selectPhotoButton.text.toString()
            listener.onComponentEdit(componentName, componentPhoto) // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.cancelcomponentButton)
        cancelButton.setOnClickListener {
            listener.onComponentCancel()
        }
        val deleteButton: Button = view.findViewById(R.id.deletecomponentButton)
        deleteButton.setOnClickListener {
            listener.onComponentDelete()
        }

        return view
    }

    interface ComponentEditListener {
        fun onComponentEdit(name: String,photo:String)
        fun onComponentCancel()
        fun onComponentDelete()
    }

    fun setComponentEditListener(listener: ComponentShowActivity) {
        this.listener = listener
    }
    private val PICK_IMAGE_REQUEST = 1

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            selectPhotoButton.setText(selectedImageUri.toString())
        }
    }
}