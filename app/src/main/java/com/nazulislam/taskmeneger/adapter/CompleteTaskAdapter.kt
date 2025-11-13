package com.nazulislam.taskmeneger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nazulislam.taskmeneger.data.Task
import com.nazulislam.taskmeneger.databinding.TaskCompleteItemBinding

class CompleteTaskAdapter(
    private val taskList: List<Task>,
    private val listener: OnTaskClickListener
) :
    RecyclerView.Adapter<CompleteTaskAdapter.CompleteTaskViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CompleteTaskViewHolder {
        return CompleteTaskViewHolder(
            binding = TaskCompleteItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: CompleteTaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.binding.apply {
            taskTitle.text = task.title

            removeBtn.setOnClickListener { listener.onRemoveClick(task.id) }
            root.setOnClickListener { listener.onTaskClick(task.id) }
        }

    }

    override fun getItemCount(): Int = taskList.size

    class CompleteTaskViewHolder(val binding: TaskCompleteItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}