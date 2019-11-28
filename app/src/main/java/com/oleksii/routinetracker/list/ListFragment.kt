package com.oleksii.routinetracker.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.createtask.CreateTaskFragment
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentListBinding

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding: FragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao

        val listViewModel = ListViewModel(dataSource, application)

        binding.listViewModel = listViewModel

        val adapter = TaskAdapter(
            TaskListener { taskId ->
                this.findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToEditTaskFragment(taskId))
            },
            DoneButtonListener { task ->
                listViewModel.onDoneTask(task)
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
            })
        binding.tasksList.adapter = adapter

        binding.addButton.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            createTaskFragment.show(fragmentManager!!, createTaskFragment.tag)
        }

        listViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}