package com.oleksii.routinetracker.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.databinding.ListItemTaskBinding
import com.oleksii.routinetracker.formatDate
import java.time.LocalDate
import java.util.*

var previousDate: LocalDate = LocalDate.MIN
var completedTasksExist: Boolean = false
var size: Int = 0

class TaskAdapter(private val clickListener: TaskListener,
                  private val doneButtonListener: DoneButtonListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>()  {

    private var tasksList: List<Task> = Collections.emptyList()

    override fun getItemCount() = tasksList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasksList[position], position, clickListener, doneButtonListener)
    }

    fun submitList(data: List<Task>?) {
        tasksList = data ?: Collections.emptyList()
        size = tasksList.size
        notifyDataSetChanged()
    }

    class ViewHolder private constructor (private val binding: ListItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Task,
            position: Int,
            clickListener: TaskListener,
            doneButtonListener: DoneButtonListener
        ) {
            if (item.stage == 0) {
                if (formatDate(item.date) == formatDate(previousDate) && position != 0)
                    binding.taskDeadline.visibility = View.GONE
                else
                    binding.taskDeadline.visibility = View.VISIBLE

                binding.taskText.paintFlags = binding.taskText.paintFlags and
                        Paint.STRIKE_THRU_TEXT_FLAG.inv()
                previousDate = item.date
                binding.taskCheck.setBackgroundResource(R.drawable.ic_radio_button_unchecked_24dp)
            } else {
                if (completedTasksExist)
                    binding.taskDeadline.visibility = View.GONE
                else
                    binding.taskDeadline.visibility = View.VISIBLE

                binding.taskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                completedTasksExist = true
                binding.taskCheck.setBackgroundResource(R.drawable.tick_30dp)
            }
            if (item.details.isNotEmpty())
                binding.taskDetails.visibility = View.VISIBLE
            binding.doneButtonListener = doneButtonListener
            binding.clickListener = clickListener
            binding.task = item
            binding.executePendingBindings()
            if (size == position + 1) {
                completedTasksExist = false
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TaskListener(val clickListener: (taskId: Long) -> Unit) {
    fun onClick(task: Task) = clickListener(task.taskId)
}

class DoneButtonListener(val clickListener: (task: Task) -> Unit) {
    fun onClick(task: Task) = clickListener(task)
}