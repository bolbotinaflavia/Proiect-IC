package com.example.homemanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.room.Camera
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomsFragment : Fragment(), RoomCreateFragment.RoomCreationListener{
    private lateinit var db: AppDatabase
    private var _roomAdapter: RoomAdapter? = null
    private var userId: Int ?= -1
    private val viewModel: UserViewModel by viewModels({requireActivity()})
val roomAdapter: RoomAdapter
    get() = _roomAdapter!!

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rooms, container, false)
    val userId = arguments?.getInt("userId",-1)
    Log.d("Received RoomsFragment","userId: $userId")
    lifecycleScope.launch {
        Log.d("RoomsFragment", "Launch - userId: $userId")
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
                val userId = viewModel.userId
                if (userId != null) {
                    db.roomDao().getRoomsByUser(userId!!)
                } else {
                    db.roomDao().getAllCameras()
                }
            }
            // Update adapter with rooms
            roomAdapter.updateRooms(rooms)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val createRoomButton: FloatingActionButton = view.findViewById(R.id.createroom)
        createRoomButton.setOnClickListener {
            val userId = viewModel.userId
            if (userId != null) {
                showRoomCreationForm(userId)
            }
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
            val userId = viewModel.userId
            putExtra("roomId", selectedRoom.id)
            putExtra("userId",userId)
        }
        startActivity(intent)
    }
    private fun showRoomCreationForm(userId: Int) {
        Log.d("RoomsFragment", "showRoomCreationForm - userId: $userId")
        val roomCreateFragment = RoomCreateFragment().apply {
            arguments = Bundle().apply {
                userId?.let {
                    putInt("userId", it)
                    Log.d("RoomsFragment", "Passing userId to RoomCreateFragment: $it")
                } ?: Log.d("RoomsFragment", "userId is null when passing to RoomCreateFragment")
            }
        }
        roomCreateFragment.setRoomCreationListener(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_rooms, roomCreateFragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onRoomCreated(name: String, description: String,photo:String,userId:Int) {
        lifecycleScope.launch {
            saveRoomToDatabase(name, description,photo,userId)
        }
    }
   override fun onRoomCancel(){
       childFragmentManager.popBackStack()
    }

    private suspend fun saveRoomToDatabase(name: String, description: String,photo:String,userId:Int) {
        val room = Camera(name = name, description = description, photo = photo,userId=userId)
        val database = AppDatabase.getInstance(requireContext())
        withContext(Dispatchers.IO) {
            database.roomDao().insertRoom(room)
        }
        Toast.makeText(requireContext(), "Room created successfully", Toast.LENGTH_SHORT).show()
        loadRoomsFromDatabase()
        childFragmentManager.popBackStack()
    }
}
