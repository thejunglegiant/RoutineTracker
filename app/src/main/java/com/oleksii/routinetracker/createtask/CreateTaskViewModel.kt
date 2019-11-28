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

    fun createTask(title: String, details: String, date: LocalDate) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val task = Task(0, title, details, date, 0)
                database.insert(task)

                onCleared()
            }
        }
    }
}