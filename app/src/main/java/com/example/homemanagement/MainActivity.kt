// MainActivity.kt
package com.example.homemanagement

import ShoppingItemFragment
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TabHost
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.homemanagement.ui.TasksFragment


class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the toolbar
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

        // Replace the fragment in the "Rooms" tab content area
        supportFragmentManager.beginTransaction()
            .replace(R.id.tab_one, RoomsFragment())
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
            R.id.btn_home->{
                supportFragmentManager.popBackStack()
                return true
            }
            R.id.RoomsMenu -> {
                Log.d("MainActivity", "Rooms menu item clicked")
                // Attempt to add RoomsFragment to fragment container view
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, RoomsFragment())
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
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "Component Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, ComponentFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.AboutMenu -> {
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "About Item is Pressed", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.ProfileMenu -> {
                // Code to be executed when the add button is clicked
                //Toast.makeText(this, "Profile Item is Pressed", Toast.LENGTH_SHORT).show()
                //return true
                Log.d("MainActivity", "Profile menu item clicked")
                // Attempt to add RoomsFragment to fragment container view
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ShopListMenu -> {
                // Code to be executed when the add button is clicked
                Toast.makeText(this, "ShopList Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.tab_host, ShoppingItemFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
//        drawerToggle.syncState()
    }

}
