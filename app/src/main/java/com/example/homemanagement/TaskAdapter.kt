package com.example.homemanagement
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.homemanagement.data.database.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.homemanagement.data.database.AppDatabase
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

class TaskAdapter(
    private val context: Context,
    private var categorizedTasks: Map<String, List<Task>>,
    private val onItemClick: (position: Int) -> Unit,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val db: AppDatabase
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_TASK = 1
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.taskTextView)
        val btnTaskShow: LinearLayout = itemView.findViewById(R.id.btn_task_show)
        val checkBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView as TextView
    }

    override fun getItemViewType(position: Int): Int {
        var index = 0
        for ((_, tasks) in categorizedTasks) {
            if (position == index) {
                return VIEW_TYPE_CATEGORY
            }
            index++
            if (position < index + tasks.size) {
                return VIEW_TYPE_TASK
            }
            index += tasks.size
        }
        throw IllegalArgumentException("Invalid position: $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                val view = LayoutInflater.from(context).inflate(R.layout.task_category_item, parent, false)
                CategoryViewHolder(view)
            }
            VIEW_TYPE_TASK -> {
                val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
                TaskViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                val category = getCategoryNameByPosition(position)
                holder.categoryTextView.text = category
            }
            is TaskViewHolder -> {
                val (category, tasks) = getCategoryAndPosition(position)
                val taskPosition = position - getCategoryStartPosition(category) - 1
                if (taskPosition < 0 || taskPosition >= tasks.size) {
                    throw IndexOutOfBoundsException("Task position $taskPosition is out of bounds for category $category with size ${tasks.size}")
                }
                val task = tasks[taskPosition]
                holder.nameTextView.text = task.name
                holder.checkBox.isChecked = task.isChecked
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                holder.dateTextView.text = dateFormatter.format(task.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())

                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        AlertDialog.Builder(context)
                            .setTitle("Finished task?")
                            .setMessage("Are you sure you want to mark this task as finished?")
                            .setPositiveButton("Yes") { _, _ ->
                                lifecycleScope.launch {
                                    withContext(Dispatchers.IO) {
                                        db.taskDao().deleteTask(task)
                                    }
                                    withContext(Dispatchers.Main) {
                                        val newTasks = tasks.toMutableList()
                                        newTasks.remove(task)
                                        updateCategorizedTasks(categorizeTasks(newTasks))
                                    }
                                }
                            }
                            .setNegativeButton("No") { _, _ ->
                                holder.checkBox.isChecked = false
                            }
                            .show()
                    }
                }

                holder.btnTaskShow.setOnClickListener {
                    onItemClick(position)
                }
            }
        }
    }*/
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> {
                val category = getCategoryNameByPosition(position)
                holder.categoryTextView.text = category
            }
            is TaskViewHolder -> {
                val (category, tasks) = getCategoryAndPosition(position)
                if (tasks.isEmpty()) return // Return if there are no tasks in this category

                val taskPosition = position - getCategoryStartPosition(category) - 1
                if (taskPosition < 0 || taskPosition >= tasks.size) {
                    throw IndexOutOfBoundsException("Task position $taskPosition is out of bounds for category $category with size ${tasks.size}")
                }
                val task = tasks[taskPosition]

                holder.nameTextView.text = task.name
                holder.checkBox.isChecked = task.isChecked
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                holder.dateTextView.text = dateFormatter.format(task.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())

                holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        AlertDialog.Builder(context)
                            .setTitle("Finished task?")
                            .setMessage("Are you sure you want to mark this task as finished?")
                            .setPositiveButton("Yes") { _, _ ->
                                lifecycleScope.launch {
                                    withContext(Dispatchers.IO) {
                                        db.taskDao().deleteTask(task)
                                    }
                                    withContext(Dispatchers.Main) {
                                        //val newTasks = tasks.toMutableList()
                                        //newTasks.remove(task)
                                    //updateCategorizedTasks(categorizeTasks(newTasks))
                                        val updatedTasksForCategory = categorizedTasks[category]?.toMutableList() ?: mutableListOf()
                                        updatedTasksForCategory.remove(task)
                                        val updatedMap = categorizedTasks.toMutableMap()
                                        updatedMap[category] = updatedTasksForCategory
                                        updateCategorizedTasks(updatedMap)
                                    }
                                }
                            }
                            .setNegativeButton("No") { _, _ ->
                                holder.checkBox.isChecked = false
                            }
                            .show()
                    }
                }

                holder.btnTaskShow.setOnClickListener {
                    onItemClick(position)
                }
            }
        }
    }


    private fun getCategoryAndPosition(position: Int): Pair<String, List<Task>> {
        var index = 0
        for ((category, tasks) in categorizedTasks) {
            if (position == index) {
                return Pair(category, tasks)
            }
            index++
            if (position < index + tasks.size) {
                return Pair(category, tasks)
            }
            index += tasks.size
        }
        throw IllegalArgumentException("Invalid position: $position")
    }

    private fun getCategoryStartPosition(category: String): Int {
        var startPosition = 0
        for ((cat, tasks) in categorizedTasks) {
            if (cat == category) {
                return startPosition
            }
            startPosition += tasks.size + 1 // +1 for the category header
        }
        throw IllegalArgumentException("Category not found: $category")
    }


    override fun getItemCount(): Int {
        return categorizedTasks.size + categorizedTasks.values.sumOf { it.size }
    }

    /*fun updateCategorizedTasks(newCategorizedTasks: Map<String, List<Task>>) {
        //categorizedTasks = newCategorizedTasks
        val updatedMap = categorizedTasks.toMutableMap()
        for ((category, tasks) in newCategorizedTasks) {
            updatedMap[category] = tasks.toMutableList()
        }
        categorizedTasks = updatedMap
        notifyDataSetChanged()
    }*/
    fun updateCategorizedTasks(newCategorizedTasks: Map<String, List<Task>>) {
        val updatedMap = mutableMapOf<String, List<Task>>()

        // Iterate through each category in the newCategorizedTasks map
        for ((category, tasks) in newCategorizedTasks) {
            // Create a copy of the tasks for the current category
            val updatedTasksForCategory = categorizedTasks[category]?.toMutableList() ?: mutableListOf()

            // Update the tasks for the current category by removing the deleted task
            updatedTasksForCategory.removeAll(tasks.filter { it !in tasks })

            // Add the updated tasks for the current category to the updated map
            updatedMap[category] = updatedTasksForCategory
        }

        // Update categorizedTasks with the updated map
        categorizedTasks = updatedMap
        notifyDataSetChanged()
    }


    fun getItem(position: Int): Task {
        val (category, tasks) = getCategoryAndPosition(position)
        return tasks[position - getCategoryStartPosition(category)]
    }

    private fun getCategoryNameByPosition(position: Int): String {
        var index = 0
        for ((category, tasks) in categorizedTasks) {
            if (position == index) {
                return category
            }
            index++
            if (position < index + tasks.size) {
                return category
            }
            index += tasks.size
        }
        throw IllegalArgumentException("Invalid position: $position")
    }

    private fun categorizeTasks(tasks: List<Task>): Map<String, List<Task>> {
        val todayTasks = mutableListOf<Task>()
        val thisWeekTasks = mutableListOf<Task>()
        val thisMonthTasks = mutableListOf<Task>()

        val today = Calendar.getInstance()
        val endOfWeek = Calendar.getInstance()
        endOfWeek.add(Calendar.DAY_OF_MONTH, 7)
        val endOfMonth = Calendar.getInstance()
        endOfMonth.add(Calendar.MONTH, 1)

        tasks.forEach { task ->
            val taskDate = Calendar.getInstance().apply { time = task.date }
            when {
                taskDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        taskDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> todayTasks.add(task)
                taskDate.before(endOfWeek) -> thisWeekTasks.add(task)
                taskDate.before(endOfMonth) -> thisMonthTasks.add(task)
            }
        }

        return mapOf(
            "Today" to todayTasks,
            "This Week" to thisWeekTasks,
            "This Month" to thisMonthTasks
        )
    }


}

