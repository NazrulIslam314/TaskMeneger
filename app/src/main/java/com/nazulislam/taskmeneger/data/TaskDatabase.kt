package com.nazulislam.taskmeneger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nazulislam.taskmeneger.utils.DateConverter

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
internal abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}