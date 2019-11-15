package com.oleksii.routinetracker.edittask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oleksii.routinetracker.database.TaskDao

class EditTaskViewModelFactory (
    private val dataSource: TaskDao,
    private val taskKey: Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java))
            return EditTaskViewModel(dataSource, taskKey) as T
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }

}