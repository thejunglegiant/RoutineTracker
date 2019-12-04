package com.oleksii.routinetracker.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.oleksii.routinetracker.database.SetOfTask
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*

class ListViewModel(
    private val database: TaskDao,
    application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var lists = database.getAllLists()
    val tasks = database.getTasksByList()

    fun onDoneTask(task: Task) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                task.stage = 1
                database.updateTask(task)
            }
        }
    }

    fun insertList() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.insertList(SetOfTask())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}