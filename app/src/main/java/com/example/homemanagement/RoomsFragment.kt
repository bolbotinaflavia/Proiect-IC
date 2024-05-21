package com.example.homemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.room.Camera
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent
class RoomsFragment : Fragment(), RoomCreateFragment.RoomCreationListener{
    private lateinit var db: AppDatabase
private var _roomAdapter: RoomAdapter? = null
val roomAdapter: RoomAdapter
    get() = _roomAdapter!!

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rooms, container, false)
        lifecycleScope.launch {
            // Get the database instance within the coroutine scope
            db = AppDatabase.getInstance(requireContext())

            // Initialize the room adapter with an empty list
            val roomGridView: GridView = view.findViewById(R.id.roomGridView)
            _roomAdapter = RoomAdapter(requireContext(), emptyList()) { position ->
                showRoom(position)
            }
            roomGridView.adapter = _roomAdapter

            _roomAdapter = roomAdapter
            // Load rooms from database
            loadRoomsFromDatabase()
        }
        return view
    }

    private fun loadRoomsFromDatabase() {
        lifecycleScope.launch {
            val rooms = withContext(Dispatchers.IO) {
                db.roomDao().getAllCameras()
            }
            // Update adapter with rooms
            roomAdapter.updateRooms(rooms)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createRoomButton: FloatingActionButton = view.findViewById(R.id.createroom)
        createRoomButton.setOnClickListener {
            showRoomCreationForm()
        }
        val roomGridView: GridView = view.findViewById(R.id.roomGridView)
        _roomAdapter = RoomAdapter(requireContext(), emptyList()) { position ->
            showRoom(position)
        }
        roomGridView.adapter = roomAdapter

        // Load rooms from database
        loadRoomsFromDatabase()

    }
    private fun showRoom(position: Int) {
        val selectedRoom = roomAdapter.getItem(position) // Get the selected room
        val intent = Intent(requireContext(), RoomShowActivity::class.java).apply {
            putExtra("roomId", selectedRoom.id)
        }
        startActivity(intent)
    }
    private fun showRoomCreationForm() {
        val roomCreateFragment = RoomCreateFragment()
        roomCreateFragment.setRoomCreationListener(this) // Set listener
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_rooms, roomCreateFragment)
            //
            .addToBackStack(null)
            .commit()
    }

    override fun onRoomCreated(name: String, description: String,photo:String) {
        lifecycleScope.launch {
            saveRoomToDatabase(name, description,photo)
        }
    }
   override fun onRoomCancel(){
       childFragmentManager.popBackStack()
    }

    private suspend fun saveRoomToDatabase(name: String, description: String,photo:String) {
        val room = Camera(name = name, description = description, photo = photo)
        val database = AppDatabase.getInstance(requireContext())
        withContext(Dispatchers.IO) {
            database.roomDao().insertRoom(room)
        }
        Toast.makeText(requireContext(), "Room created successfully", Toast.LENGTH_SHORT).show()
        loadRoomsFromDatabase()
        childFragmentManager.popBackStack()
    }

}
