package com.oleksii.routinetracker.bottommenu

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.bottomactions.BottomMenuViewModel
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentAddListBinding
import com.oleksii.routinetracker.focusAndShowKeyboard
import com.oleksii.routinetracker.hideKeyboard
import com.oleksii.routinetracker.list.ListFragment

class AddListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentAddListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_list, container, false)
        val application: Application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val arguments = AddListFragmentArgs.fromBundle(requireArguments())
        val currentListId = arguments.id
        val bottomMenuViewModel = BottomMenuViewModel(dataSource)

        focusAndShowKeyboard(this.activity, binding.editTitle)

        bottomMenuViewModel.newListId.observe(this, Observer {
            ListFragment.currentListId = it
        })

        binding.cancelAction.setOnClickListener {
            findNavController().navigate(R.id.action_addListFragment_to_list_fragment)
        }

        binding.doneButton.setOnClickListener {
            var title = binding.editTitle.text.toString()
            if (binding.editTitle.text.isEmpty())
                title = ""

            bottomMenuViewModel.createList(currentListId, title)
            findNavController().navigate(R.id.action_addListFragment_to_list_fragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard(this.activity)
    }
}