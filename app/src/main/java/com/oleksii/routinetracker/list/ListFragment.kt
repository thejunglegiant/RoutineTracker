package com.oleksii.routinetracker.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.bottommenu.BottomMenuFragment
import com.oleksii.routinetracker.createtask.CreateTaskFragment
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentListBinding

lateinit var binding: FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        listViewModel = ListViewModel(dataSource, application)
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

        binding.listViewModel = listViewModel
        binding.tasksList.adapter = adapter

        binding.addButton.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            createTaskFragment.show(fragmentManager!!, createTaskFragment.tag)
        }

        binding.moreActions.setOnClickListener {
            val bottomMenuFragment = BottomMenuFragment()
            bottomMenuFragment.show(fragmentManager!!, bottomMenuFragment.tag)
        }

        listViewModel.tasks.observe(viewLifecycleOwner, Observer { data ->
            amountOfTasks = 0
            amountOfCompletedTasks = 0
            data.filter { it.listId == currentListId }
            data.forEach { task ->
                amountOfTasks++
                if (task.stage == 1)
                    amountOfCompletedTasks++
            }
            adapter.submitList(data)

        })

        listViewModel.list.observe(viewLifecycleOwner, Observer { list ->
            if (list == null) {
                listViewModel.insertList()
            } else {
                currentListId = list.listId
                binding.listTitle.text = list.title
            }
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        listViewModel.setLastOpenedList(currentListId)
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
    }
}