package com.oleksii.routinetracker.edittask

import androidx.lifecycle.ViewModel
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*
import java.time.LocalDate

class EditTaskViewModel(val database: TaskDao, private val taskKey: Long) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var task = database.getTaskWithId(taskKey)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun updateTask(currentListId: Long, title: String, details: String, date: LocalDate, stage: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.updateTask(Task(taskKey, currentListId, title, details, date, stage))
                onCleared()
            }
        }
    }

    fun deleteTask() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteTask(taskKey)
                onCleared()
            }
        }
    }
}