// ShoppingItemFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.R
import com.example.homemanagement.ShoppingItemCreateFragment
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.Element
import com.example.homemanagement.data.database.shoppingItem.ShoppingItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit


class ShoppingItemFragment : Fragment(), ShoppingItemAdapter.OnDeleteClickListener,
    ShoppingItemCreateFragment.ShoppingItemCreationListener {

    private lateinit var db: AppDatabase
    private lateinit var shoppingItemAdapter: ShoppingItemAdapter
    private lateinit var addButton: FloatingActionButton
    private lateinit var confettiView: KonfettiView
    private lateinit var party: Party
    private lateinit var confettiText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_item, container, false)

        addButton = view.findViewById(R.id.fabAddItem)
        confettiView = view.findViewById(R.id.confettiView) // Initialize the confetti animation view
        confettiText = view.findViewById(R.id.confettiText)

        party = Party(
            speed = 15f,
            maxSpeed = 45f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0x33dc11, 0xf646ff, 0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 750, TimeUnit.MILLISECONDS).max(250),
            position = Position.Relative(0.5, 0.3)
        )

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
            showConfettiEffect()
        }
    }

    private suspend fun deleteShoppingItemFromDatabase(item: ShoppingItem) {
        withContext(Dispatchers.IO) {
            db.shoppingItemDao().deleteShoppingItem(item)
        }
        loadShoppingItemsFromDatabase()
    }

    private fun showConfettiEffect() {
        confettiText.visibility = View.VISIBLE
        confettiView.visibility = View.VISIBLE
        confettiView.start(party)
        confettiView.postDelayed({
            confettiView.visibility = View.GONE
            confettiText.visibility = View.GONE
        }, 750) // Show for 3 seconds
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createShoppingItemButton: FloatingActionButton = view.findViewById(R.id.fabAddItem)
        createShoppingItemButton.setOnClickListener {
            showShoppingItemCreationForm()
        }
    }

    private fun showShoppingItemCreationForm() {
        addButton.visibility = View.GONE
        val shoppingItemCreateFragment = ShoppingItemCreateFragment()
        shoppingItemCreateFragment.setShoppingItemCreationListener(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_shopping_item, shoppingItemCreateFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onShoppingItemCreated(name: String) {
        addButton.visibility = View.VISIBLE
        lifecycleScope.launch {
            saveShoppingItemToDatabase(name)
        }
    }

    override fun onShoppingItemCancel() {
        addButton.visibility = View.VISIBLE
        childFragmentManager.popBackStack()
    }

    suspend fun saveShoppingItemToDatabase(name: String) {
        val shoppingItem = ShoppingItem(name = name)
        withContext(Dispatchers.IO) {
            db.shoppingItemDao().insertShoppingItem(shoppingItem)
        }
        loadShoppingItemsFromDatabase()
        childFragmentManager.popBackStack()
    }

}
