package com.oleksii.routinetracker.createtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oleksii.routinetracker.database.TaskDao
import com.oleksii.routinetracker.edittask.EditTaskViewModel

class CreateTaskViewModelFactory(
    private val dataSource: TaskDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java))
            return CreateTaskViewModel(dataSource) as T
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}