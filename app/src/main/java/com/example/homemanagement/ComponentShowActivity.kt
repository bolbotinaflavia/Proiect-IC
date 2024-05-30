package com.example.homemanagement

import AboutFragment
import ShoppingItemFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.Element
import com.example.homemanagement.data.database.component.Component
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

class ComponentShowActivity: AppCompatActivity(), ComponentEditFragment.ComponentEditListener,ElementCreateFragment.ElementCreationListener,ElementEditFragment.ElementEditListener {
    private lateinit var toolbar: Toolbar
    private lateinit var db: AppDatabase
    private var componentId:Int=-1
    private var userId:Int?=null
    private lateinit var component: Component
    private lateinit var elementAdapter: ElementAdapter
    private val elements = mutableListOf<Element>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.component_show)


        //Set up the toolbar
        toolbar = findViewById(R.id.toolbar_component)
        setSupportActionBar(toolbar)

        componentId = intent.getIntExtra("componentId", -1)
        userId=intent.getIntExtra("userId",-1)

        lifecycleScope.launch {
            db = AppDatabase.getInstance(applicationContext)

            if (componentId != -1) {

                loadComponentFromDatabase(componentId)

            }
            val nameTextView: TextView = findViewById(R.id.components_text_view)


            val editcomponentButton: ImageButton = findViewById(R.id.button_edit_component)
            editcomponentButton.setOnClickListener {
                if (::component.isInitialized) {
                    showEditCreationForm()
                } else {
                    Log.e("componentShowActivity", "component is not initialized.")
                }

            }
            val recyclerView: RecyclerView = findViewById(R.id.elementRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@ComponentShowActivity)
            elementAdapter = ElementAdapter(
                elements,
                onEditClick = { element -> showEditElementForm(element) },
                onAddToShopListClick = { element -> addToShopList(element) }
            )
            recyclerView.adapter = elementAdapter

            val addElementButton: Button = findViewById(R.id.addElementButton)
            addElementButton.setOnClickListener {
                showElementCreationForm()
            }

            // Load elements associated with the component
            loadElementsFromDatabase()
        }
    }


    private fun addToShopList(element: Element) {
        // Handle adding element to shopping list
    }


    private fun showElementCreationForm() {
        val elementCreateFragment = ElementCreateFragment()
        elementCreateFragment.setElementCreationListener(this) // Set listener
        val bundle = Bundle().apply {
            putInt("componentId",  componentId ?: 0)
        }
        elementCreateFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.header_component, elementCreateFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onElementCancel() {
        supportFragmentManager.popBackStack()
    }

    override fun onElementDelete(element: Element) {
        lifecycleScope.launch {
            deleteElementFromDatabase(element)
        }
    }

    private suspend fun updateElementInDatabase(element: Element) {
        withContext(Dispatchers.IO) {
            db.elementDao().updateElement(element)
        }
        Toast.makeText(this, "Element updated successfully", Toast.LENGTH_SHORT).show()
        loadElementsFromDatabase()
        supportFragmentManager.popBackStack()
    }
    // Add this method to load the Element object before showing the edit form
    private fun showEditElementForm(element: Element) {
        val elementEditFragment = ElementEditFragment.newInstance(element)
        elementEditFragment.setElementEditListener(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.header_component, elementEditFragment)
            .addToBackStack(null)
            .commit()
    }



    override fun onElementEdit(element: Element, name: String) {
        lifecycleScope.launch {
            element.name = name
            saveElementToDatabase(element)
        }
    }

    override fun onElementCreated(name: String, componentId: Int) {
        lifecycleScope.launch {
            val newElement = Element(0, name, componentId) // Assuming 0 is a placeholder for a new element
            saveElementToDatabase(newElement)
        }
    }


    private suspend fun deleteElementFromDatabase(element: Element) {
        withContext(Dispatchers.IO) {
            element.id?.let { db.elementDao().deleteElement(it) }
        }
        Toast.makeText(this, "Element deleted successfully", Toast.LENGTH_SHORT).show()
        loadElementsFromDatabase()
        supportFragmentManager.popBackStack()
    }
    private suspend fun saveElementToDatabase(element: Element) {
        val database = AppDatabase.getInstance(this)
        withContext(Dispatchers.IO) {
            if (element.id > 0) {
                // Update existing element
                database.elementDao().updateElement(element)
            } else {
                // Insert new element
                database.elementDao().insertElement(element)
            }
        }
        Toast.makeText(this, "Element saved successfully", Toast.LENGTH_SHORT).show()
        loadElementsFromDatabase()
        supportFragmentManager.popBackStack()
    }

    private fun loadElementsFromDatabase(){
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(this@ComponentShowActivity)
            val loadedElements = withContext(Dispatchers.IO) {
                db.elementDao().getElementsByComponent(componentId)
            }
            elements.clear()
            elements.addAll(loadedElements)
            elementAdapter.notifyDataSetChanged()
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
                /// Log.d("MainActivity", "Rooms menu item clicked")
                // Attempt to add RoomsFragment to fragment container view
                val roomsFragment = RoomsFragment().apply {
                    arguments = Bundle().apply {
                        userId?.let {
                            putInt("userId", it)
                            //Log.d("MainActivity-userId", "userId: $it")
                        }
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_component, roomsFragment)
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
                    .replace(R.id.show_component, componentFragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.AboutMenu -> {
                Toast.makeText(this, "About Item is Pressed", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_component, AboutFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ProfileMenu -> {
                Log.d("MainActivity", "Profile menu item clicked")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_component, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            R.id.ShopListMenu -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.header_component, ShoppingItemFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showEditCreationForm() {
        val componentName = component.name ?: ""
        val componentPhoto=component.photo?:""
        val componentEditFragment = ComponentEditFragment.newInstance(componentName,componentPhoto)
        componentEditFragment.setComponentEditListener(this)
        //Log.d("componentShowActivity", "Showing componentEditFragment with name: $componentName, description: $componentDescription")
        supportFragmentManager.beginTransaction()
            .replace(R.id.header_component, componentEditFragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
//        drawerToggle.syncState()
    }

    override fun onComponentEdit(name: String,photo:String) {
        lifecycleScope.launch {
            saveComponentToDatabase(name,photo)
        }
    }

    override fun onComponentCancel() {
        supportFragmentManager.popBackStack()
    }

    override fun onComponentDelete() {
        lifecycleScope.launch {
            deleteComponentFromDatabase()
        }
    }

    private fun loadComponentFromDatabase(id: Int) {
        lifecycleScope.launch {
            val loadedcomponent = withContext(Dispatchers.IO) {
                db.componentDao().getComponent(id)
            }
            if (loadedcomponent != null) {
                component = loadedcomponent
                updateUI(component)
            } else {
                Log.e("componentShowActivity", "Failed to load component with ID: $id")
            }
        }
    }

    private fun updateUI(component: Component) {
        val nameTextView: TextView = findViewById(R.id.components_text_view)
        nameTextView.text = component.name

//        val descriptionTextView: TextView = findViewById(R.id.component_description)
//        descriptionTextView.text = component.description
    }

    private suspend fun deleteComponentFromDatabase() {
        withContext(Dispatchers.IO) {
            db.componentDao().deleteComponent(component)
        }
        Toast.makeText(this, "component deleted successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private suspend fun saveComponentToDatabase(name: String,photo:String) {
        component.name = name
        component.photo=photo
        withContext(Dispatchers.IO) {
            db.componentDao().updateComponent(component)
        }
        Toast.makeText(this, "component updated successfully", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
        updateUI(component)
    }
}