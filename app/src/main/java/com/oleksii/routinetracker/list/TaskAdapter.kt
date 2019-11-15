package com.oleksii.routinetracker.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.databinding.ListItemTaskBinding
import java.time.LocalDate

class TaskAdapter(val clickListener: TaskListener, val doneButtonListener: DoneButtonListener) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback())  {

    companion object {
        var previousDate: LocalDate? = null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position, clickListener, doneButtonListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Task,
            position: Int,
            clickListener: TaskListener,
            doneButtonListener: DoneButtonListener
        ) {
            if (item.date == previousDate && position != 0)
                binding.taskDeadline.visibility = View.GONE
            if (item.details != "")
                binding.taskDetails.visibility = View.VISIBLE
            previousDate = item.date
            binding.task = item
            binding.clickListener = clickListener
            binding.doneButtonListener = doneButtonListener
            binding.executePendingBindings()
        }

        // function needs to be in a companion object so it can be called on the ViewHolder class,
        // not called on a ViewHolder instance.
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}

class TaskListener(val clickListener: (taskId: Long) -> Unit) {
    fun onClick(task: Task) = clickListener(task.taskId)
}

class DoneButtonListener(val clickListener: (taskId: Long) -> Unit) {
    fun onClick(task: Task) = clickListener(task.taskId)
}