package com.nazulislam.taskmeneger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM taskDb")
    fun getAllTask(): List<Task>

    @Query("SELECT * FROM taskDb WHERE id = :taskId")
    fun findTaskById(taskId: Int): Task?

    @Query("UPDATE taskDb SET isCompleted = :isCompleted WHERE id = :taskId")
    fun updateTaskCompletionStatus(taskId: Int, isCompleted: Boolean)

    @Query("SELECT * FROM taskDb WHERE isCompleted = 0")
    fun getPendingTasks(): List<Task>

    @Query("SELECT * FROM taskDb WHERE isCompleted = 1")
    fun getCompletedTasks(): List<Task>

    @Query("DELETE FROM taskDb WHERE id = :taskId")
    fun deleteTaskById(taskId: Int)

    @Query("SELECT * FROM taskDb WHERE isCompleted = 0 ORDER BY date")
    fun getPendingTasksSortedByDate(): List<Task>

    @Query("SELECT * FROM taskDb WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchTasks(query: String): List<Task>
}