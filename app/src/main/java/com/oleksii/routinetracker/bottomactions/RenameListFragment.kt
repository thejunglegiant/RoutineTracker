package com.oleksii.routinetracker.bottomactions

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentRenameListBinding
import com.oleksii.routinetracker.focusAndShowKeyboard
import com.oleksii.routinetracker.hideKeyboard

class RenameListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentRenameListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_rename_list, container, false)
        val application: Application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val bottomMenuViewModel = BottomActionsViewModel(dataSource)
        val arguments = RenameListFragmentArgs.fromBundle(requireArguments())
        val currentListId = arguments.id

        focusAndShowKeyboard(this.activity, binding.editTitle)

        binding.cancelAction.setOnClickListener {
            findNavController().navigate(R.id.action_renameListFragment_to_list_fragment)
        }

        binding.doneButton.setOnClickListener {
            var title = binding.editTitle.text.toString()
            if (binding.editTitle.text.isEmpty())
                title = ""

            bottomMenuViewModel.renameCurrentList(currentListId, title)
            findNavController().navigate(R.id.action_renameListFragment_to_list_fragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard(this.activity)
    }
}