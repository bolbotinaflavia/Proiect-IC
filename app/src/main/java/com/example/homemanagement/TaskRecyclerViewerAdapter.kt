/*import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homemanagement.R
import com.example.homemanagement.data.database.Task

class TaskRecyclerViewAdapter(private val context: Context, private var items: List<Task>, private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_task_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener.onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemNameTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    fun updateTasks(newItems: List<Task>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(item: Task)
    }
}
*/