package com.example.homemanagement

import AboutFragment
import ShoppingItemFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TabHost
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.room.Camera
import com.example.homemanagement.ui.TasksFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.homemanagement.LoginActivity as LoginActivity

class RoomShowActivity : AppCompatActivity(),RoomEditFragment.RoomEditListener {
    private lateinit var toolbar: Toolbar
    private var roomId: Int = -1
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var db: AppDatabase
    private lateinit var room: Camera
    private  var userId: Int? =null
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.room_show)


        //Set up the toolbar
        toolbar = findViewById(R.id.toolbar_room)
        setSupportActionBar(toolbar)

        roomId = intent.getIntExtra("roomId", -1)
        userId=intent.getIntExtra("userId",-1)

        lifecycleScope.launch {
            db = AppDatabase.getInstance(this@RoomShowActivity)

            if (roomId != -1) {

              loadRoomFromDatabase(roomId)

            }
            val nameTextView: TextView = findViewById(R.id.rooms_text_view)



            val editRoomButton: ImageButton = findViewById(R.id.button_edit_room)
            editRoomButton.setOnClickListener {
                if (::room.isInitialized) {
                    showEditCreationForm()
                } else {
                    Log.e("RoomShowActivity", "Room is not initialized.")
                }

            }
            val tabHost: TabHost = findViewById(R.id.tab_host_room)
            tabHost.setup()


            // Create a tab for Components
            var spec: TabHost.TabSpec = tabHost.newTabSpec("Components")
            spec.setContent(R.id.tab_one_room)
            spec.setIndicator("Components")
            tabHost.addTab(spec)

            // Create a tab for Tasks
            spec = tabHost.newTabSpec("Tasks")
            spec.setContent(R.id.tab_two_room)
            spec.setIndicator("Tasks")
            tabHost.addTab(spec)


            val componentFragment = ComponentFragment().apply {
                arguments = Bundle().apply {
                    //val userId= viewModel.userId
                    userId?.let { putInt("userId", it) }
                    roomId?.let{putInt("roomId",it)}
                    Log.d("RoomShowActivity", "userId: $userId")
                }
            }
            // Replace the fragment in the "Rooms" tab content area
            supportFragmentManager.beginTransaction()
                .replace(R.id.tab_one_room, componentFragment)
                .commit()

            supportFragmentManager.commit {
                replace(R.id.tab_two_room, TasksFragment())
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.btn_home->{

                val intent = Intent(this, MainActivity::class.java).apply{
                    putExtra("userId",userId)
                }
                startActivity(intent)
                return true
            }
            R.id.RoomsMenu -> {

               // Log.d("MainActivity", "Rooms menu item clicked")
                // Attempt to add RoomsFragment to fragment container view
                //val userId=viewModel.userId
                val roomsFragment = RoomsFragment().apply {
                    arguments = Bundle().apply {
                        userId?.let {
                            putInt("userId", it)
                            //Log.d("MainActivity-userId", "userId: $it")
                        }
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_room, roomsFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.TasksMenu -> {
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "Task Item is Pressed", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.ComponentsMenu -> {
                //val userId=viewModel.userId
                val componentFragment = ComponentFragment().apply {
                    arguments = Bundle().apply {
                        userId?.let {
                            putInt("userId", it)
                            Log.d("ShowRoom-to-fragment-userId", "userId: $it")
                        }
                        putInt("roomId",-1)
                    }
                }
                // Code to be executed when the add button is clicked
               // Toast.makeText(this, "Component Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_room, componentFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.AboutMenu -> {
                Toast.makeText(this, "About Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_room, AboutFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ProfileMenu -> {
                Log.d("MainActivity", "Profile menu item clicked")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_room, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ShopListMenu -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_room, ShoppingItemFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showEditCreationForm() {
        val roomName = room.name ?: ""
        val roomDescription = room.description ?: ""
        val roomPhoto=room.photo?:""
        val roomEditFragment = RoomEditFragment.newInstance(roomName, roomDescription,roomPhoto)
        roomEditFragment.setRoomEditListener(this) // Set listener
        //Log.d("RoomShowActivity", "Showing RoomEditFragment with name: $roomName, description: $roomDescription")
        supportFragmentManager.beginTransaction()
            .replace(R.id.header_room, roomEditFragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
//        drawerToggle.syncState()
    }

    override fun onRoomEdit(name: String, description: String,photo:String) {
        lifecycleScope.launch {
            saveRoomToDatabase(name, description,photo)
        }
    }

    override fun onRoomCancel() {
        supportFragmentManager.popBackStack()
    }

    override fun onRoomDelete() {
        lifecycleScope.launch {
            deleteRoomFromDatabase()
        }
    }

    private fun loadRoomFromDatabase(id: Int) {
        lifecycleScope.launch {
            val loadedRoom = withContext(Dispatchers.IO) {
                db.roomDao().getCamera(id)
            }
            if (loadedRoom != null) {
                room = loadedRoom
                updateUI(room)
            } else {
                Log.e("RoomShowActivity", "Failed to load room with ID: $id")
            }
        }
    }

    private fun updateUI(room: Camera) {
        val nameTextView: TextView = findViewById(R.id.rooms_text_view)
        nameTextView.text = room.name

//        val descriptionTextView: TextView = findViewById(R.id.room_description)
//        descriptionTextView.text = room.description
    }

    private suspend fun deleteRoomFromDatabase() {
        withContext(Dispatchers.IO) {
            db.roomDao().deleteRoom(room)
        }
        Toast.makeText(this, "Room deleted successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

    private suspend fun saveRoomToDatabase(name: String, description: String,photo:String) {
        room.name = name
        room.description = description
        room.photo=photo
        withContext(Dispatchers.IO) {
            db.roomDao().updateRoom(room)
        }
        Toast.makeText(this, "Room updated successfully", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
        updateUI(room)
    }
}