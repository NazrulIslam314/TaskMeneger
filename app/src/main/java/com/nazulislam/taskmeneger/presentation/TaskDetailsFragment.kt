package com.nazulislam.taskmeneger.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.nazulislam.taskmeneger.data.Task
import com.nazulislam.taskmeneger.data.TaskDatabase
import com.nazulislam.taskmeneger.databinding.FragmentTaskDetailsBinding
import com.nazulislam.taskmeneger.utils.showDatePickerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.format


class TaskDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailsBinding
    private var selectedDate: Date? = null
    private val db: TaskDatabase by lazy {
        Room.databaseBuilder(
            requireContext().applicationContext, TaskDatabase::class.java, "task_database"
        ).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupToolbar()
        binding.date.setOnClickListener {
            showDatePickerDialog(
                childFragmentManager,
                { selection -> updateDateButtonText(selection) })
        }


        binding.saveBtn.setOnClickListener {
            val title = binding.inTitle.text.toString()
            val description = binding.inDescription.text.toString()
            val date = binding.date.text.toString()
            if (validateInput(title, description, date)) {
                saveTask(
                    Task(
                        title = title,
                        description = description,
                        date = selectedDate
                    )
                )
            }

        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun updateDateButtonText(selection: Long) {
        val date = Date(selection)
        selectedDate = date
        binding.date.text = formantDate(date)
    }

    private fun validateInput(
        title: String,
        description: String,
        date: String,
    ): Boolean {
        return !(title.isBlank() || description.isBlank() || date.isBlank() || date == "Select Date")
    }


    private fun saveTask(task: Task) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.taskDao().addTask(task)
            }
            Toast.makeText(requireContext(), "Task Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun formantDate(date : Date): String{
        return  SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    }


}