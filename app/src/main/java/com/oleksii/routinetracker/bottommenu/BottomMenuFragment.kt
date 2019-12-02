package com.oleksii.routinetracker.bottomactions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.bottommenu.ClickListener
import com.oleksii.routinetracker.bottommenu.ListAdapter
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentBottomMenuBinding
import com.oleksii.routinetracker.list.ListFragment
import com.oleksii.routinetracker.list.ListFragmentDirections

class BottomMenuFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentBottomMenuBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bottom_menu, container, false)

        // Create an instance of the ViewModel.
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val bottomMenuViewModel = BottomMenuViewModel(dataSource)
        val currentListId = ListFragment.currentListId

        val adapter = ListAdapter(
            ClickListener { listId ->
                ListFragment.currentListId = listId
                bottomMenuViewModel.setCurrentList(currentListId, listId)
                this.dismiss()
            }
        )

        binding.titlesList.adapter = adapter

        binding.addList.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToAddListFragment(currentListId)
            )
            this.dismiss()
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        bottomMenuViewModel.lists.observe(this, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }
}