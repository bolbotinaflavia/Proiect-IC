// MainActivity.kt
package com.example.homemanagement

import AboutFragment
import ShoppingItemFragment
import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TabHost
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import androidx.fragment.app.viewModels
import com.example.homemanagement.TasksFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider



class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private var userId: Int? = null
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (viewModel.userId == null) {
            userId = intent.getIntExtra("userId", -1)
            if (userId != -1) {
                viewModel.userId = userId
            }
        } else {
            userId = viewModel.userId
        }

        // Log the userId to ensure it's being set correctly
        Log.d("MainActivity", "UserId: $userId")

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up the tabs
        val tabHost: TabHost = findViewById(R.id.tab_host)
        tabHost.setup()

        // Create a tab for RoomsFragment
        var spec: TabHost.TabSpec = tabHost.newTabSpec("Rooms")
        spec.setContent(R.id.tab_one)
        spec.setIndicator("Rooms")
        tabHost.addTab(spec)

        // Create a tab for TasksFragment
        spec = tabHost.newTabSpec("Tasks")
        spec.setContent(R.id.tab_two)
        spec.setIndicator("Tasks")
        tabHost.addTab(spec)

        val roomsFragment = RoomsFragment().apply {
            arguments = Bundle().apply {
                userId?.let { putInt("userId", it) }
                Log.d("Main-UserId", "Retrieved userId: $userId")
            }
        }
        // Replace the fragment in the "Rooms" tab content area
        supportFragmentManager.beginTransaction()
            .replace(R.id.tab_one, roomsFragment)
            .commit()

        // Replace the fragment in the "Tasks" tab content area
        supportFragmentManager.beginTransaction()
            .replace(R.id.tab_two, TasksFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_home -> {
                supportFragmentManager.popBackStack()
                return true
            }
            R.id.RoomsMenu -> {
                // Log.d("MainActivity", "Rooms menu item clicked")
                // Attempt to add RoomsFragment to fragment container view
                val userId=viewModel.userId
                val roomsFragment = RoomsFragment().apply {
                    arguments = Bundle().apply {
                        userId?.let {
                            putInt("userId", it)
                            //Log.d("MainActivity-userId", "userId: $it")
                        }
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, roomsFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.TasksMenu -> {

                // Code to be executed when the add button is clicked
                //Toast.makeText(this, "Task Item is Pressed", Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "Tasks menu item clicked")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, TasksFragment())
                    .addToBackStack(null)
                    .commit()

                Toast.makeText(this, "Task Item is Pressed", Toast.LENGTH_SHORT).show()

                return true
            }
            R.id.ComponentsMenu -> {
                val componentFragment = ComponentFragment().apply {
                    arguments = Bundle().apply {
                        userId?.let {
                            putInt("userId", it)
                            //Log.d("MainActivity-userId", "userId: $it")
                        }
                        putInt("roomId",-1)
                    }
                }
                // Code to be executed when the add button is clicked
                // Toast.makeText(this, "Component Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, componentFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.AboutMenu -> {
                Toast.makeText(this, "About Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, AboutFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ProfileMenu -> {
                Log.d("MainActivity", "Profile menu item clicked")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ShopListMenu -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, ShoppingItemFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setUserId(userId: Int) {
        viewModel.userId = userId
    }

    fun getUserId(): Int? {
        return viewModel.userId
    }
}
