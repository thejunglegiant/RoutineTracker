package com.oleksii.routinetracker.list

import android.app.Application
import android.util.Log
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

    val tasks = database.getAllTasks()
    var list = database.getListLastOpened()

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

    fun setLastOpenedList(currentListId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val list = database.getListById(currentListId)
                list.opened = true
                database.insertList(list)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}