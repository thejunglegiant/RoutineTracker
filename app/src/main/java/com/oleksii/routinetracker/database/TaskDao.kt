package com.oleksii.routinetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDate
import java.util.*

@Dao
@OnConflictStrategy
interface TaskDao {

    @Insert()
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Query("SELECT * FROM tasks_table WHERE taskId = :key")
    fun getTaskWithId(key: Long): LiveData<Task>

    @Query("SELECT * FROM tasks_table ORDER BY task_stage, CASE WHEN task_date IS :date THEN 1 ELSE 0 END, task_date")
    fun getAllTasks(date: LocalDate = LocalDate.MIN): LiveData<List<Task>>

    @Query("DELETE FROM tasks_table WHERE taskId = :key")
    fun deleteTask(key: Long)
}