package com.oleksii.routinetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDate

@Dao
@OnConflictStrategy
interface TaskDao {

    @Insert
    fun insertTask(task: Task)

    @Insert
    fun insertList(list: SetOfTask)

    @Update
    fun updateTask(task: Task)

    @Update
    fun updateList(list: SetOfTask)

    @Query("SELECT * FROM tasks_table WHERE taskId = :key")
    fun getTaskWithId(key: Long): LiveData<Task>

    @Query("SELECT tasks_table.* FROM lists_table LEFT JOIN tasks_table WHERE last_opened " +
            "AND lists_table.listId = tasks_table.listId " +
            "ORDER BY task_stage, CASE WHEN task_date IS :date THEN 1 ELSE 0 END, task_date")
    fun getTasksByList(date: LocalDate = LocalDate.MIN): LiveData<List<Task>>

    @Query("DELETE FROM tasks_table WHERE taskId = :key")
    fun deleteTask(key: Long)

    @Query("DELETE FROM tasks_table WHERE task_stage = 1")
    fun deleteAllCompletedTasks()

    @Query("DELETE FROM lists_table WHERE listId = :listId")
    fun deleteCurrentListById(listId: Long)

    @Query("DELETE FROM tasks_table WHERE listId = :listId")
    fun deleteAllTasksOfCurrentList(listId: Long)

    @Query("SELECT * FROM lists_table WHERE last_opened")
    fun getLastOpenedList(): LiveData<SetOfTask>

    @Query("SELECT listId FROM lists_table WHERE last_opened")
    fun getLastOpenedListId(): Long

    @Query("SELECT * FROM lists_table WHERE listId = :listId")
    fun getListById(listId: Long): SetOfTask

    @Query("SELECT * FROM lists_table ORDER BY listId")
    fun getAllLists(): LiveData<List<SetOfTask>>

    @Query("SELECT * FROM lists_table WHERE listId = 1")
    fun getDefaultList(): SetOfTask
}