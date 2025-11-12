package com.nazulislam.taskmeneger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert()
    fun addTask(task: Task)

    @Query("SELECT * FROM taskDb")
    fun getAllTask(): List<Task>


}