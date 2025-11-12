package com.nazulislam.taskmeneger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert()
    fun addTask(task: Task)


    @Update()
    fun updateTask(task: Task)

    @Query("SELECT * FROM taskDb")
    fun getAllTask(): List<Task>


    @Query("SELECT * FROM taskDb WHERE id = :taskId")
    fun findTaskById(taskId: Int): Task?


}