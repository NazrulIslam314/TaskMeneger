package com.nazulislam.taskmeneger.domain

interface OnTaskClickListener {
    fun onTaskClick(taskId: Int)
    fun onRemoveClick(taskId: Int)
    fun onTaskCheckedChange(taskId: Int, isChecked: Boolean)
}