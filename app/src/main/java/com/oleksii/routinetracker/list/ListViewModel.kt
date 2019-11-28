package com.oleksii.routinetracker.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*

class ListViewModel(
    val database: TaskDao,
    application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val tasks = database.getAllTasks()

    fun onDoneTask(task: Task) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                task.stage = 1
                database.update(task)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}