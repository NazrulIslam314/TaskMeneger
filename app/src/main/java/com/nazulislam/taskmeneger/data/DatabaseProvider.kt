package com.nazulislam.taskmeneger.data

import android.content.Context
import androidx.room.Room
import com.nazulislam.taskmeneger.utils.Constants.DATABASE_NAME

object DatabaseProvider {
    @Volatile
    private var INSTANCE: TaskDatabase? = null

    fun getDatabase(context: Context): TaskDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                DATABASE_NAME
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
