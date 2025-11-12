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
import com.nazulislam.taskmeneger.R
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


class TaskDetailsFragment : Fragment() {
    private lateinit var binding: FragmentTaskDetailsBinding
    private var selectedDate: Date? = null
    private val db: TaskDatabase by lazy {
        Room.databaseBuilder(
            requireContext().applicationContext, TaskDatabase::class.java, "task_database"
        ).build()
    }


    private val taskIdToEdit: Int by lazy {
        arguments?.getInt("taskId") ?: -1
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check the args
        if (taskIdToEdit != -1) {
            setEditTaskText(taskIdToEdit)
            binding.saveBtn.text = getString(R.string.update)
        }

        setupToolbar()
        binding.date.setOnClickListener {
            showDatePickerDialog(
                childFragmentManager,
                { selection -> updateDateButtonText(selection)}, preselectedDate = selectedDate?.time)
        }


        binding.saveBtn.setOnClickListener {
            saveTask()
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
        binding.date.text = formatDate(date)
    }

    private fun validateInput(
        title: String,
        description: String,
        date: String,
    ): Boolean {
        return !(title.isBlank() || description.isBlank() || date.isBlank() || date == "Select Date")
    }


    private fun saveTask() {
        // select the input field
        val title = binding.inTitle.text.toString()
        val description = binding.inDescription.text.toString()
        val date = binding.date.text.toString()

        // validate the input
        if (validateInput(title, description, date)) {

            // try to store or update
            lifecycleScope.launch {
                val massage = if (taskIdToEdit == -1) {
                    withContext(Dispatchers.IO) {
                        val task = Task(
                            title = title,
                            description = description,
                            date = selectedDate
                        )
                        db.taskDao().addTask(task)
                        "Task Saved"
                    }
                } else {
                    withContext(Dispatchers.IO) {

                        // check the existingTask is not null
                        val existingTask = db.taskDao().findTaskById(taskIdToEdit)
                        if (existingTask != null) {
                            val updatedTask = existingTask.copy(
                                title = title,
                                description = description,
                                date = selectedDate
                            )
                            db.taskDao().updateTask(updatedTask)
                            "Task Updated"
                        } else {
                            "Task Not Found"
                        }
                    }
                }

                Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }


    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    }


    private fun setEditTaskText(id: Int) {
        lifecycleScope.launch {
            val task = withContext(Dispatchers.IO) {
                db.taskDao().findTaskById(id)
            }
            selectedDate = task?.date
            withContext(Dispatchers.Main) {
                with(binding) {
                    inTitle.setText(task?.title)
                    inDescription.setText(task?.description)
                    date.text = task?.date?.let { formatDate(it) } ?: "Select Date"
                }
            }
        }
    }


}