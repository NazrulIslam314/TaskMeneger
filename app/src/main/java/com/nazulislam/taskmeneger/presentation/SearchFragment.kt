package com.nazulislam.taskmeneger.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nazulislam.taskmeneger.R
import com.nazulislam.taskmeneger.adapter.OnTaskClickListener
import com.nazulislam.taskmeneger.adapter.TaskAdapter
import com.nazulislam.taskmeneger.data.DatabaseProvider
import com.nazulislam.taskmeneger.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment(), OnTaskClickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var taskAdapter: TaskAdapter

    private val db by lazy { DatabaseProvider.getDatabase(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        taskAdapter = TaskAdapter(emptyList(), this@SearchFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        // auto open the keyboard
        binding.searchEditText.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

        // Setup the recyclerView
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
        loadTask()

        // listen the user input change
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString().trim()
                if (query.isEmpty()) {
                    loadTask()
                } else {
                    searchTask(query)
                }
            }
        })
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadTask() {
        lifecycleScope.launch {
            val tasks = withContext(Dispatchers.IO) {
                db.taskDao().getPendingTasks()
            }

            withContext(Dispatchers.Main) {
                taskAdapter = TaskAdapter(tasks, this@SearchFragment)
                binding.searchRecyclerView.adapter = taskAdapter
            }
        }
    }

    private fun searchTask(query: String) {
        lifecycleScope.launch {
            val searchResults = withContext(Dispatchers.IO) {
                db.taskDao().searchTasks(query)
            }

            withContext(Dispatchers.Main) {
                taskAdapter = TaskAdapter(searchResults, this@SearchFragment)
                binding.searchRecyclerView.adapter = taskAdapter
            }
        }
    }

    override fun onTaskClick(taskId: Int) {
        val bundle = Bundle().apply {
            putInt("taskId", taskId)
        }
        findNavController().navigate(R.id.action_searchFragment_to_taskDeatilsFragment, bundle)
    }

    // Remove functionality is not needed in Search Fragment
    // Users should go to the main fragment to remove tasks
    override fun onRemoveClick(taskId: Int) {
        // Not implemented in search context
    }

    override fun onTaskCheckedChange(taskId: Int, isChecked: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.taskDao().updateTaskCompletionStatus(taskId, isChecked)
            withContext(Dispatchers.Main) {
                loadTask()
                navigateUpWithToast("Task Completed Success")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadTask()
    }

    private fun navigateUpWithToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }
}
