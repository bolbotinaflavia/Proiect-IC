// ShoppingItemFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.R
import com.example.homemanagement.ShoppingItemCreateFragment
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.shoppingItem.ShoppingItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingItemFragment : Fragment(), ShoppingItemAdapter.OnDeleteClickListener,
    ShoppingItemCreateFragment.ShoppingItemCreationListener{

    private lateinit var db: AppDatabase
    private lateinit var shoppingItemAdapter: ShoppingItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_item, container, false)

        lifecycleScope.launch {
            db = AppDatabase.getInstance(requireContext())

            val shoppingListView: ListView = view.findViewById(R.id.shoppingListView)
            shoppingItemAdapter = ShoppingItemAdapter(requireContext(), emptyList(), this@ShoppingItemFragment)
            shoppingListView.adapter = shoppingItemAdapter

            loadShoppingItemsFromDatabase()
        }

        return view
    }

    private fun loadShoppingItemsFromDatabase() {
        lifecycleScope.launch {
            val shoppingItems = withContext(Dispatchers.IO) {
                db.shoppingItemDao().getAllShoppingItems()
            }
            shoppingItemAdapter.updateShoppingItems(shoppingItems)
        }
    }

    override fun onDeleteClick(item: ShoppingItem) {
        lifecycleScope.launch {
            deleteShoppingItemFromDatabase(item)
        }
    }

    private suspend fun deleteShoppingItemFromDatabase(item: ShoppingItem) {
        withContext(Dispatchers.IO) {
            db.shoppingItemDao().deleteShoppingItem(item)
        }
        loadShoppingItemsFromDatabase()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createShoppingItemButton: FloatingActionButton = view.findViewById(R.id.fabAddItem)
        createShoppingItemButton.setOnClickListener {
            showShoppingItemCreationForm()
        }
    }

    private fun showShoppingItemCreationForm() {
        val shoppingItemCreateFragment = ShoppingItemCreateFragment()
        shoppingItemCreateFragment.setShoppingItemCreationListener(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_shopping_item, shoppingItemCreateFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onShoppingItemCreated(name: String) {
        lifecycleScope.launch {
            saveShoppingItemToDatabase(name)
        }
    }

    override fun onShoppingItemCancel() {
        childFragmentManager.popBackStack()
    }

    private suspend fun saveShoppingItemToDatabase(name: String) {
        val shoppingItem = ShoppingItem(name = name)
        withContext(Dispatchers.IO) {
            db.shoppingItemDao().insertShoppingItem(shoppingItem)
        }
        loadShoppingItemsFromDatabase()
        childFragmentManager.popBackStack()
    }

}
