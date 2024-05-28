package com.example.homemanagement

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
import com.example.homemanagement.data.database.component.Component
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class ComponentFragment() : Fragment(), ComponentCreateFragment.ComponentCreationListener {
    private lateinit var db: AppDatabase
    private var roomId: Int ?= 0
    private var userId by Delegates.notNull<Int>()
    private var _componentAdapter: ComponentAdapter? = null
    private val viewModel: UserViewModel by viewModels({requireActivity()})
    val componentAdapter: ComponentAdapter
        get() = _componentAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_components, container, false)
        roomId = arguments?.getInt("roomId") ?:-1
        userId=arguments?.getInt("userId")?:-1
        lifecycleScope.launch {
            // Get the database instance within the coroutine scope
            db = AppDatabase.getInstance(requireContext())

            // Initialize the room adapter with an empty list
            val componentGridView: GridView = view.findViewById(R.id.componentGridView)
            _componentAdapter = ComponentAdapter(requireContext(), emptyList()) { position ->
                showComponent(position)
            }
            componentGridView.adapter = _componentAdapter
            _componentAdapter=componentAdapter

            // Load rooms from database
            loadComponentsFromDatabase()
        }
        return view
    }
    private fun loadComponentsFromDatabase() {
        lifecycleScope.launch {
            val components = withContext(Dispatchers.IO) {
                Log.d("Components show", "userId:$userId roomId: $roomId")
                //val userId = viewModel.userId
                if (roomId != -1) {
                    db.componentDao().getComponentsByRoom(roomId!!)
                }
                else {
                    if (userId == null)
                        db.componentDao().getAllComponents()
                    else {
                            Log.d("Components by user", "userId:$userId")
                            db.componentDao().getComponentsByUser(userId)
                        }
                }
            }
                // Update adapter with rooms

            componentAdapter.updateComponents(components)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createComponentButton: FloatingActionButton = view.findViewById(R.id.createcomponent)
        createComponentButton.setOnClickListener {
            //val userId = viewModel.userId
            Log.d("To-Fragment-create","userId: $userId")
            userId?.let { showComponentCreationForm(it) }
        }
        val componentGridView: GridView = view.findViewById(R.id.componentGridView)
        _componentAdapter = ComponentAdapter(requireContext(), emptyList()) { position ->
            showComponent(position)
        }
        componentGridView.adapter = componentAdapter

        // Load rooms from database
        loadComponentsFromDatabase()

    }
    private fun showComponent(position: Int){
        val selectedComponent = componentAdapter.getItem(position) // Get the selected room
        val intent = Intent(requireContext(), ComponentShowActivity::class.java).apply {
            putExtra("componentId", selectedComponent.id)
            putExtra("userId",userId)
            Log.d("To-Show-Component","dddd")
        }
        startActivity(intent)
    }
    private fun showComponentCreationForm(userId:Int) {
        val componentCreateFragment = ComponentCreateFragment()
        componentCreateFragment.setComponentCreationListener(this) // Set listener
        val bundle = Bundle().apply {
            Log.d("To-Create-Component","userId: $userId roomId: $roomId")
            putInt("roomId",  roomId ?: -1)
            putInt("userId", userId)

        }
        componentCreateFragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_components, componentCreateFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onComponentCreated(name: String, photo: String, roomId: Int,userId:Int) {
        lifecycleScope.launch {
            saveComponentToDatabase(name,photo,roomId,userId)
        }
    }
    override fun onComponentCancel(){
        childFragmentManager.popBackStack()
    }

    private suspend fun saveComponentToDatabase(name: String,photo:String,roomId:Int,userId:Int) {
        val component = Component(name = name,photo=photo, roomId = if (roomId == -1) null else roomId,userId=userId)
        val database = AppDatabase.getInstance(requireContext())
        withContext(Dispatchers.IO) {
            database.componentDao().insertComponent(component)
        }
        Toast.makeText(requireContext(), "Component created successfully", Toast.LENGTH_SHORT).show()
        loadComponentsFromDatabase()
        childFragmentManager.popBackStack()
    }

}