// MainActivity.kt
package com.example.homemanagement

import android.os.Bundle
import android.view.MenuItem
import android.widget.TabHost
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.example.homemanagement.ui.TasksFragment


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var tab_host: TabHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up the drawer layout and toggle
        drawerLayout = findViewById(R.id.drawer)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Enable the home button and set up navigation button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

}
