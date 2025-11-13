package com.nazulislam.taskmeneger.adapter

interface OnTaskClickListener {
    fun onTaskClick(taskId: Int)
    fun onRemoveClick(taskId: Int)
    fun onTaskCheckedChange(taskId: Int, isChecked: Boolean)
}