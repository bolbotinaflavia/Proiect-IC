package com.example.homemanagement.Room_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.homemanagement.R

class RoomCreateFragment : Fragment() {
    private lateinit var submitButton: Button
    private lateinit var roomNameEditText: EditText
    private lateinit var roomDescriptionEditText: EditText
    private lateinit var listener: RoomCreationListener // Add listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.new_room, container, false)

        submitButton = view.findViewById(R.id.submitButton)
        roomNameEditText = view.findViewById(R.id.roomNameEditText)
        roomDescriptionEditText = view.findViewById(R.id.roomDescriptionEditText)

        submitButton.setOnClickListener {
            val roomName = roomNameEditText.text.toString()
            val roomDescription = roomDescriptionEditText.text.toString()
            listener.onRoomCreated(roomName, roomDescription) // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            (requireParentFragment() as? RoomsFragment)?.onRoomCancel()
        }

        return view
    }

    interface RoomCreationListener {
        fun onRoomCreated(name: String, description: String)
        fun onRoomCancel()
    }

    fun setRoomCreationListener(listener: RoomCreationListener) {
        this.listener = listener
    }
}
