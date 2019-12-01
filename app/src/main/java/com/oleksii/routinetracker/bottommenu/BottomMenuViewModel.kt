package com.oleksii.routinetracker.bottomactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.oleksii.routinetracker.database.SetOfTask
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*

class BottomMenuViewModel(val database: TaskDao) : ViewModel() {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var lists = database.getAllLists()

    private val _newListId = MutableLiveData<Long>()
    val newListId
            get() = _newListId

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setCurrentList(currentListId: Long, chosenListId: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val currentList = database.getListById(currentListId)
                currentList.opened = false
                database.updateList(currentList)
                val chosenList = database.getListById(chosenListId)
                chosenList.opened = true
                database.updateList(chosenList)
            }
        }
    }

    fun createList(currentListId: Long, title: String) {
        uiScope.launch {
            val newList = SetOfTask(0, title, true)
            _newListId.value = newList.listId
            withContext(Dispatchers.IO) {
                val currentList = database.getListById(currentListId)
                currentList.opened = false
                database.updateList(currentList)
                database.insertList(newList)
            }
        }
    }
}