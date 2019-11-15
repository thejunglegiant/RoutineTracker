package com.oleksii.routinetracker.list


import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.createtask.CreateTaskFragment
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentListBinding
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding: FragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao

        val viewModelFactory = ListViewModelFactory(dataSource, application)

        val listViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)

        binding.listViewModel = listViewModel

        binding.setLifecycleOwner(this)

        val adapter = TaskAdapter(
            TaskListener { taskId ->
                this.findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToEditTaskFragment(taskId))
            },
            DoneButtonListener { taskId ->
                listViewModel.onDoneTask(taskId)
                Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
            })

        binding.tasksList.adapter = adapter

        listViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

        binding.addButton.setOnClickListener {
            val createTaskFragment = CreateTaskFragment()
            createTaskFragment.show(fragmentManager!!, createTaskFragment.tag)
        }

        return binding.root
    }
}