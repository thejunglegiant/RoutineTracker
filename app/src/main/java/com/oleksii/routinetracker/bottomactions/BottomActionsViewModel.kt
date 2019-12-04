package com.oleksii.routinetracker.bottomactions

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.oleksii.routinetracker.database.SetOfTask
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*

class BottomActionsViewModel(val database: TaskDao) : ViewModel() {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun deleteAllCompletedTasks() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteAllCompletedTasks()
                onCleared()
            }
        }
    }

    fun deleteCurrentList(currentListId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteAllTasksOfCurrentList(currentListId)
                val defaultList = database.getDefaultList()
                defaultList.opened = true
                database.updateList(defaultList)
                database.deleteCurrentListById(currentListId)
            }
        }
    }

    fun renameCurrentList(currentListId: Long, title: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val list = database.getListById(currentListId)
                if (title != "")
                    list.title = title
                database.updateList(list)
            }
        }
    }
}