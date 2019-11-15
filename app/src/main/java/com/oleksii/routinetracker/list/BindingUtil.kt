package com.oleksii.routinetracker.list

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.formatDate
import java.time.LocalDate

@BindingAdapter("taskTitle")
fun TextView.setTaskTitle(item: Task?) {
    item?.let {
        text = it.title
    }
}

@BindingAdapter("taskDetails")
fun TextView.setTaskDetails(item: Task?) {
    item?.let {
        text = it.details
    }
}

@BindingAdapter("taskDate")
fun TextView.setTaskDateAsString(item: Task?) {
    item?.let {
        text = formatDate(it.date)
    }
}