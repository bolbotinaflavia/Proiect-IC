// ShoppingItemAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.homemanagement.R
import com.example.homemanagement.data.database.shoppingItem.ShoppingItem

class ShoppingItemAdapter(private val context: Context, private var items: List<ShoppingItem>, private val onDeleteClickListener: OnDeleteClickListener) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ShoppingItem = items[position]

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_shopping, parent, false)
        val itemName = view.findViewById<TextView>(R.id.itemNameTextView)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        itemName.text = items[position].name

        deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(items[position])
        }

        return view
    }

    fun updateShoppingItems(newItems: List<ShoppingItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(item: ShoppingItem)
    }
}
