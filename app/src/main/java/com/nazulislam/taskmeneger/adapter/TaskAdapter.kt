package com.nazulislam.taskmeneger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nazulislam.taskmeneger.data.Task
import com.nazulislam.taskmeneger.databinding.TaskItemBinding
import com.nazulislam.taskmeneger.utils.Constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(private val taskList: List<Task>, private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TaskViewHolder {
        return TaskViewHolder(
            binding = TaskItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.apply {
            taskTitle.text = task.title

            if (task.date != null) {
                val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                taskDueDate.text = sdf.format(task.date)
                taskDueDate.visibility = View.VISIBLE
            } else {
                taskDueDate.visibility = View.GONE
            }

            taskStatusChip.text = if (task.isCompleted) "Completed" else "In Progress"

            root.setOnClickListener { listener.onTaskClick(task.id) }

            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                listener.onTaskCheckedChange(task.id, isChecked)
            }
        }
    }

    override fun getItemCount(): Int = taskList.size

    class TaskViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)
}