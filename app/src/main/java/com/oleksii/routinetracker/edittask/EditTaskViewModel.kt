package com.oleksii.routinetracker.edittask

import androidx.lifecycle.ViewModel
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*
import java.time.LocalDate

class EditTaskViewModel(val database: TaskDao, val taskKey: Long) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var task = database.getTaskWithId(taskKey)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun updateTask(title: String, details: String, date: LocalDate, stage: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.update(Task(taskKey, title, details, date, stage))
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