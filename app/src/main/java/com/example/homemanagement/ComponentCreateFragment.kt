package com.example.homemanagement


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class ComponentCreateFragment : Fragment() {
    private lateinit var submitButton: Button
    private lateinit var componentNameEditText: EditText
    private lateinit var selectPhotoButton: Button
    private lateinit var listener: ComponentCreationListener // Add listener
    private var selectedImageUri: Uri? = null
    private var roomId: Int = 0
    private var userId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.new_component, container, false)
        roomId = arguments?.getInt("roomId") ?: 0
        userId = arguments?.getInt("userId")?:0

        submitButton = view.findViewById(R.id.submitButton_component)
        componentNameEditText = view.findViewById(R.id.componentNameEditText)
        selectPhotoButton = view.findViewById(R.id.selectcomponentPhotoButton)

        selectPhotoButton.setOnClickListener {
            openImageChooser()
        }
        submitButton.setOnClickListener {
            val componentName = componentNameEditText.text.toString()
            val componentPhoto=selectedImageUri?.toString() ?: ""
            Log.d("In-Create-Fragment", "userId: $userId, roomId: $roomId")
            if (userId != null) {
                listener.onComponentCreated(componentName, componentPhoto, roomId, userId!!)
            } else {
                Log.e("ComponentCreateFragment", "User ID is null")
            }
        }
        val cancelButton: Button = view.findViewById(R.id.cancelButton_component)
        cancelButton.setOnClickListener {
            (requireParentFragment() as? ComponentFragment)?.onComponentCancel()
        }

        return view
    }

    interface ComponentCreationListener {
        fun onComponentCreated(name: String, photo: String, roomId: Int,userId:Int)
        fun onComponentCancel()
    }

    fun setComponentCreationListener(listener: ComponentCreationListener) {
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
            selectedImageUri = data.data
            selectPhotoButton.text = "Image Selected"
        }
    }
}
