package com.oleksii.routinetracker.bottommenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oleksii.routinetracker.database.SetOfTask
import com.oleksii.routinetracker.databinding.ListItemListBinding
import java.util.*

class ListAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>()  {

    private var setOfTaskList: List<SetOfTask> = Collections.emptyList()

    override fun getItemCount() = setOfTaskList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(setOfTaskList[position], clickListener)
    }

    fun submitList(data: List<SetOfTask>) {
        setOfTaskList = data
        notifyDataSetChanged()
    }

    class ViewHolder private constructor (private val binding: ListItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: SetOfTask,
            clickListener: ClickListener
        ) {
            binding.setOfTask = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemListBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class ClickListener(val clickListener: (listId: Long) -> Unit) {
    fun onClick(list: SetOfTask) = clickListener(list.listId)
}