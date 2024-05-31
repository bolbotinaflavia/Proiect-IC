package com.example.homemanagement

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.Task
import com.example.homemanagement.data.database.shoppingItem.ShoppingItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class TasksFragment : Fragment(), TaskCreateFragment.TaskCreationListener{
    private lateinit var db: AppDatabase
    private var roomId: Int ?= null
    private var _taskAdapter: TaskAdapter? = null
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRecyclerView: RecyclerView
    private var isChecked: Boolean ?= null
    private var categorizedTasks: Map<String, List<Task>> = mutableMapOf()
    //private var categorizedTasks: Map<String, List<Task>> = mutableMapOf()



    //val taskAdapter: TaskAdapter
      //  get() = _taskAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        roomId = arguments?.getInt("roomId")
        isChecked = arguments?.getBoolean("isChecked")
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }
    private fun loadTasksFromDatabase() {
        lifecycleScope.launch {
            val tasks = withContext(Dispatchers.IO) {
                // Fetch tasks from the database
                if (roomId != null) {
                    db.taskDao().getTasksByRoom(roomId!!)
                } else {
                    db.taskDao().getAllTasks()
                }
            }

            categorizedTasks = categorizeTasks(tasks)
            taskAdapter = TaskAdapter(requireContext(), categorizedTasks, { position ->
                showTask(position)
            }, lifecycleScope, db)
            taskRecyclerView.adapter = taskAdapter
        }
    }

    /*override fun onDeleteClick(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { dialog, _ ->
                lifecycleScope.launch {
                    deleteTaskFromDatabase(task)
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }*/

    /*private suspend fun deleteTaskFromDatabase(task: Task) {
        withContext(Dispatchers.IO) {
            db.taskDao().deleteTask(task)
        }
        val updatedTasks = loadTasksFromDatabase() // Load updated tasks from the database
        val updatedCategorizedTasks = categorizeTasks(updatedTasks)
       // val updatedCategorizedTasks = loadTasksFromDatabase();
        taskAdapter.updateCategorizedTasks(updatedCategorizedTasks)
        //loadTasksFromDatabase()
        Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show()
    }*/

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
    private fun updateTasks(newTasks: List<Task>) {
        val categorizedTasks = categorizeTasks(newTasks)
        taskAdapter.updateCategorizedTasks(categorizedTasks)
    }



    private fun showTask(position: Int) {
        val selectedTask = _taskAdapter?.getItem(position)
        //val selectedTask = taskAdapter.getItem(position)
        Toast.makeText(requireContext(), "Selected task: ${selectedTask?.name}", Toast.LENGTH_SHORT).show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            // Get the database instance within the coroutine scope
            db = AppDatabase.getInstance(requireContext())
            loadTasksFromDatabase()
        }
        val createTaskButton: FloatingActionButton = view.findViewById(R.id.New_Task)
        createTaskButton.setOnClickListener {
          showTaskCreationForm()
    }
    }
    private fun showTaskCreationForm() {
        val taskCreateFragment = TaskCreateFragment()
        taskCreateFragment.setTaskCreationListener(this) // Set listener
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, taskCreateFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onTaskCreated(name: String,data: Date) {
        lifecycleScope.launch {
            saveTaskToDatabase(name,data)
            loadTasksFromDatabase()
            //val tasks = loadTasksFromDatabase()
            //val updatedCategorizedTasks = categorizeTasks(tasks)
            //taskAdapter.updateCategorizedTasks(updatedCategorizedTasks)
        }
    }
    override fun onTaskCancel(){
        childFragmentManager.popBackStack()
    }

    private suspend fun saveTaskToDatabase(name: String, data: Date) {
        val task = Task(name = name, date = data)
        //val database = AppDatabase.getInstance(requireContext())
        withContext(Dispatchers.IO) {
            db.taskDao().insertTask(task)
        }
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), "Task created successfully", Toast.LENGTH_SHORT).show()
            childFragmentManager.popBackStack() // Close the task creation fragment
        }
    }

}
