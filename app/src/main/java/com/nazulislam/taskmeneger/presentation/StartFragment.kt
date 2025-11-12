package com.nazulislam.taskmeneger.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.nazulislam.taskmeneger.R
import com.nazulislam.taskmeneger.data.TaskDatabase
import com.nazulislam.taskmeneger.databinding.FragmentStartBinding
import com.nazulislam.taskmeneger.domain.OnTaskClickListener
import com.nazulislam.taskmeneger.domain.TaskAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StartFragment : Fragment(), OnTaskClickListener
{

    private lateinit var binding: FragmentStartBinding
    private lateinit var taskAdapter : TaskAdapter
    private  val db : TaskDatabase by lazy {
        Room.databaseBuilder(
            requireContext().applicationContext, TaskDatabase::class.java, "task_database"
        ).build()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        taskAdapter = TaskAdapter(emptyList(), this@StartFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  navigate to taskDetailsFragment
        binding.addTaskBtn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_taskDeatilsFragment)
        }


        // Setup RecyclerView
        binding.TaskRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }


    private fun loadTask() {
        lifecycleScope.launch {
            val task = withContext(Dispatchers.IO) {
                db.taskDao().getAllTask()
            }
            taskAdapter = TaskAdapter(task, this@StartFragment)
            binding.TaskRecyclerView.adapter = taskAdapter
        }
    }


    override fun onResume() {
        super.onResume()
        loadTask()
    }


    override fun onTaskClick(taskId: Int) {
        val bundle = Bundle().apply {
            putInt("taskId", taskId)
        }
        findNavController().navigate(R.id.action_startFragment_to_taskDeatilsFragment, bundle)
    }

}