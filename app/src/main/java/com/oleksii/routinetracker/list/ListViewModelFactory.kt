package com.oleksii.routinetracker.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oleksii.routinetracker.database.TaskDao

class ListViewModelFactory (
    private val dataSource: TaskDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java))
            return ListViewModel(dataSource, application) as T
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }

}