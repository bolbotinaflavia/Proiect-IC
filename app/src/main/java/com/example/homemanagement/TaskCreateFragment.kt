package com.example.homemanagement

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.homemanagement.data.database.AppDatabase
import com.example.homemanagement.data.database.Task
import com.example.homemanagement.data.database.TaskDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskCreateFragment : Fragment() {
    private lateinit var submitButton: Button
    private lateinit var taskNameEditText: EditText
    //private lateinit var taskDateEditText:String = ""
    //private lateinit var taskDescriptionEditText: EditText
    private lateinit var listener: TaskCreationListener // Add listener
    //private var selectedDeadline: String = ""
    private lateinit var deadlineEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_create, container, false)

        submitButton = view.findViewById(R.id.btnCreate)
        taskNameEditText = view.findViewById(R.id.etTaskName)
        //taskDateEditText = view.findViewById(R.id.taskDateEditText)
        //taskDescriptionEditText = view.findViewById(R.id.etDescription)

        deadlineEditText = view.findViewById(R.id.etDeadline)

        deadlineEditText.setOnClickListener {
            showDatePickerDialog()
        }

        submitButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()
            val taskDate = getSelectedDate()
            //val taskDate = taskDateEditText.text.toString()
            //val taskDate = deadlineEditText.text.toString()
            //val taskDescription = taskDescriptionEditText.text.toString()
            listener.onTaskCreated(taskName,taskDate) // Notify listener
        }
        val cancelButton: Button = view.findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            (requireParentFragment() as? TasksFragment)?.onTaskCancel()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Set the selected date to the EditText
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.time)
            deadlineEditText.setText(formattedDate)
        }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    private fun getSelectedDate(): Date {
        val formattedDate = deadlineEditText.text.toString()
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(formattedDate) ?: Date()
    }
    interface TaskCreationListener {
        fun onTaskCreated(name: String,data:Date)
        fun onTaskCancel()
    }

    fun setTaskCreationListener(listener: TaskCreationListener) {
        this.listener = listener
    }

}