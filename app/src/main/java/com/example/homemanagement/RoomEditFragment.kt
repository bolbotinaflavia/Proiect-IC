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

class RoomEditFragment :Fragment() {
    private lateinit var editButton:Button
    private lateinit var roomNameEditText: EditText
    private lateinit var roomDescriptionEditText: EditText
    private lateinit var selectPhotoButton: Button
    private lateinit var listener: RoomEditListener // Add listener

    companion object {
        private const val ARG_ROOM_NAME = "room_name"
        private const val ARG_ROOM_DESCRIPTION = "room_description"
        private const val ARG_ROOM_PHOTO="room_photo"

        fun newInstance(roomName: String, roomDescription: String,roomPhoto:String): RoomEditFragment {
            val fragment = RoomEditFragment()
            val args = Bundle()
            args.putString(ARG_ROOM_NAME, roomName)
            args.putString(ARG_ROOM_DESCRIPTION, roomDescription)
            args.putString(ARG_ROOM_PHOTO,roomPhoto)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.room_edit, container, false)

        editButton = view.findViewById(R.id.editButton)
        roomNameEditText = view.findViewById(R.id.roomNameEditText)
        roomDescriptionEditText = view.findViewById(R.id.roomDescriptionEditText)
        //roomPhotoEditText = view.findViewById(R.id.roomPhotoEditText)
        selectPhotoButton = view.findViewById(R.id.selectPhotoButton)

        selectPhotoButton.setOnClickListener {
            openImageChooser()
        }

        // Set initial values from arguments
        val initialroomName = arguments?.getString(ARG_ROOM_NAME) ?: ""
        val initialroomDescription = arguments?.getString(ARG_ROOM_DESCRIPTION) ?: ""
        val initialroomPhoto=arguments?.getString(ARG_ROOM_PHOTO)?:""
        roomNameEditText.setText(initialroomName)
        roomDescriptionEditText.setText(initialroomDescription)
        selectPhotoButton.setText(initialroomPhoto)

        editButton.setOnClickListener {
            val roomName = roomNameEditText.text.toString()
            val roomDescription = roomDescriptionEditText.text.toString()
            val roomPhoto=selectPhotoButton.text.toString()
            listener.onRoomEdit(roomName, roomDescription,roomPhoto) // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            listener.onRoomCancel()
        }
        val deleteButton:Button= view.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            listener.onRoomDelete()
        }

        return view
    }

    interface RoomEditListener {
        fun onRoomEdit(name: String, description: String,photo:String)
        fun onRoomCancel()
        fun onRoomDelete()
    }

    fun setRoomEditListener(listener: RoomShowActivity) {
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