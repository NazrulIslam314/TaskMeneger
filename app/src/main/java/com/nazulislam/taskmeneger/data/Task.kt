package com.nazulislam.taskmeneger.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "taskDb")
data class Task(
    @PrimaryKey(true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val date: Date?,
    val isCompleted: Boolean= false
)
