package com.oleksii.routinetracker.createtask

import androidx.lifecycle.ViewModel
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*
import java.time.LocalDate

class CreateTaskViewModel(val database: TaskDao) : ViewModel() {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun createTask(currentListId: Long, title: String, details: String, date: LocalDate) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val task = Task(0, currentListId, title, details, date)
                database.insertTask(task)

                onCleared()
            }
        }
    }
}