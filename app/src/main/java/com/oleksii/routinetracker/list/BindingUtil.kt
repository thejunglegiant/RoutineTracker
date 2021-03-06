package com.oleksii.routinetracker.list

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.SetOfTask
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.formatDate

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
        text = when (it.stage) {
            0 -> formatDate(it.date)
            else -> context.getString(R.string.completed)
        }
    }
}

@BindingAdapter("listTitle")
fun TextView.setListTitle(item: SetOfTask?) {
    text = item?.title
}