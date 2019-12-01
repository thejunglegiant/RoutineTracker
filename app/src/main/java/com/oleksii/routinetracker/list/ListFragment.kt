package com.oleksii.routinetracker.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.bottomactions.BottomActionsFragment
import com.oleksii.routinetracker.bottomactions.BottomMenuFragment
import com.oleksii.routinetracker.createtask.CreateTaskFragment
import com.oleksii.routinetracker.database.Task
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentListBinding
import java.util.*

private lateinit var binding: FragmentListBinding
private lateinit var listViewModel: ListViewModel

class ListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val adapter = TaskAdapter(
            TaskListener { taskId ->
                this.findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToEditTaskFragment(taskId)
                )
            },
            DoneButtonListener { task ->
                listViewModel.onDoneTask(task)
                showSnackBar(getString(R.string.you_did_it))
            })
        binding.tasksList.adapter = adapter

        listViewModel = ListViewModel(dataSource, application)
        binding.listViewModel = listViewModel

        binding.addButton.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            createTaskFragment.show(fragmentManager!!, createTaskFragment.tag)
        }

        binding.moreActions.setOnClickListener {
            val bottomActionsFragment = BottomActionsFragment()
            bottomActionsFragment.show(fragmentManager!!, bottomActionsFragment.tag)
        }

        binding.menuButton.setOnClickListener {
            val bottomMenuFragment = BottomMenuFragment()
            bottomMenuFragment.show(fragmentManager!!, bottomMenuFragment.tag)
        }

        listViewModel.lists.observe(viewLifecycleOwner, Observer { lists ->
            if (lists == Collections.EMPTY_LIST) {
                listViewModel.insertList()
            } else {
                lists.forEach { list ->
                    if (list.opened) {
                        currentListId = list.listId
                        binding.list = list
                        submitAll(listViewModel.tasks.value, adapter)
                    }
                }
            }
        })

        listViewModel.tasks.observe(this, Observer {
            submitAll(it, adapter)
        })

        return binding.root
    }

    companion object {
        var amountOfCompletedTasks: Int = 0
        var amountOfTasks: Int = 0
        var currentListId: Long = 0
        fun showSnackBar(string: String) {
            Snackbar.make(
                binding.tasksList,
                string,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        fun submitAll(data: List<Task>?, adapter: TaskAdapter) {
            amountOfTasks = 0
            amountOfCompletedTasks = 0
            data?.forEach { task ->
                amountOfTasks++
                if (task.stage == 1)
                    amountOfCompletedTasks++
            }

            adapter.submitList(data)
        }
    }
}