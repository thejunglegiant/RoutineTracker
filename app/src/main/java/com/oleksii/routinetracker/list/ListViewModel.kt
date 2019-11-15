package com.oleksii.routinetracker.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*

class ListViewModel(
    val database: TaskDao,
    application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val tasks = database.getAllTasks()

    fun onDoneTask(taskId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteTask(taskId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}