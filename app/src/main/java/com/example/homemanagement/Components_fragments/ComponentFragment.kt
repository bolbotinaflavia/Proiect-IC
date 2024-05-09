package com.example.homemanagement.Components_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.R
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.component.Component
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class ComponentFragment: Fragment(), ComponentCreateFragment.ComponentCreationListener  {
    private lateinit var db: AppDatabase
    private lateinit var componentAdapter: ComponentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_components, container, false)
        lifecycleScope.launch {
            // Get the database instance within the coroutine scope
            db = AppDatabase.getInstance(requireContext())

            // Initialize the room adapter with an empty list
            val componentGridView: GridView = view.findViewById(R.id.componentGridView)
            componentAdapter = ComponentAdapter(requireContext(), emptyList())
            componentGridView.adapter = componentAdapter

            // Load rooms from database
            loadComponentsFromDatabase()
        }
        return view
    }
    private fun loadComponentsFromDatabase() {
        lifecycleScope.launch {
            val components = withContext(Dispatchers.IO) {
                db.componentDao().getAllComponents()
            }
            // Update adapter with rooms
            componentAdapter.updateComponents(components)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createRoomButton: FloatingActionButton = view.findViewById(R.id.createcomponent)
        createRoomButton.setOnClickListener {
            showComponentCreationForm()
        }

    }
    private fun showComponent(){

    }
    private fun showComponentCreationForm() {
        val componentCreateFragment = ComponentCreateFragment()
        componentCreateFragment.setComponentCreationListener(this) // Set listener
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_components, ComponentCreateFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onComponentCreated(name: String) {
        lifecycleScope.launch {
            saveComponentToDatabase(name)
        }
    }
    override fun onComponentCancel(){
        childFragmentManager.popBackStack()
    }

    private suspend fun saveComponentToDatabase(name: String) {
        val component = Component(name = name)
        val database = AppDatabase.getInstance(requireContext())
        withContext(Dispatchers.IO) {
            database.componentDao().insertComponent(component)
        }
        loadComponentsFromDatabase()
        childFragmentManager.popBackStack()
    }

}