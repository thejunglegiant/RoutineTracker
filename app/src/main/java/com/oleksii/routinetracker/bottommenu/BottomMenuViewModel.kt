package com.oleksii.routinetracker.bottommenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDao
import kotlinx.coroutines.*
import java.time.LocalDate
import java.util.*

class BottomMenuViewModel(val database: TaskDao) : ViewModel() {
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
}