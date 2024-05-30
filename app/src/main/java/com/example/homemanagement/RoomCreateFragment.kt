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
import android.widget.Toast
import androidx.fragment.app.Fragment

class RoomCreateFragment : Fragment() {
    private lateinit var submitButton: Button
    private lateinit var roomNameEditText: EditText
    private lateinit var roomDescriptionEditText: EditText
    private lateinit var selectPhotoButton: Button
    private lateinit var listener: RoomCreationListener // Add listener
    private var selectedImageUri: Uri? = null
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.new_room, container, false)
        userId = arguments?.getInt("userId")
        userId?.let {
            Log.d("Room-Create-UserId", "userId: $userId")
            //Toast.makeText(context, "UserId is transmitted", Toast.LENGTH_SHORT).show()
        }
        submitButton = view.findViewById(R.id.submitButton)
        roomNameEditText = view.findViewById(R.id.roomNameEditText)
        roomDescriptionEditText = view.findViewById(R.id.roomDescriptionEditText)
        selectPhotoButton = view.findViewById(R.id.selectPhotoButton)

        selectPhotoButton.setOnClickListener {
            openImageChooser()
        }
        submitButton.setOnClickListener {
            val roomName = roomNameEditText.text.toString()
            val roomDescription = roomDescriptionEditText.text.toString()
            val roomPhoto=selectedImageUri?.toString() ?: ""
           // val userId=userId

            if (userId != null) {
                listener.onRoomCreated(roomName, roomDescription,roomPhoto,userId!!)
            } // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            (requireParentFragment() as? RoomsFragment)?.onRoomCancel()
        }

        return view
    }
    interface RoomCreationListener {
        fun onRoomCreated(name: String, description: String,photo:String,userId:Int)
        fun onRoomCancel()
    }

    fun setRoomCreationListener(listener: RoomCreationListener) {
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
