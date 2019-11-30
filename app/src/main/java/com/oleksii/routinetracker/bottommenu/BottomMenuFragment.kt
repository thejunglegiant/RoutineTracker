package com.oleksii.routinetracker.bottommenu

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.routinetracker.R
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
        val amountOfCompletedTasks = ListFragment.amountOfCompletedTasks
        val amountOfTasks = ListFragment.amountOfTasks
        val currentListId = ListFragment.currentListId
        binding.buttomMenuViewModel = bottomMenuViewModel

        val stageDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(context)

        val stageClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (amountOfCompletedTasks != 0) {
                            bottomMenuViewModel.deleteAllCompletedTasks()
                            ListFragment.showSnackBar("Completed tasks were deleted")
                        }
                    }
                }
            }

        val listClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        bottomMenuViewModel.deleteCurrentList(currentListId)
                        ListFragment.showSnackBar("List were successfully deleted")
                    }
                }
            }

        stageDialog.setTitle("Delete all completed tasks?")
            .setMessage("There are $amountOfCompletedTasks " +
                "completed ${if (amountOfCompletedTasks> 1) "tasks" else "task"} " +
                    "that will be permanently removed.")
            .setPositiveButton("Delete", stageClickListener)
            .setNegativeButton("Cancel", stageClickListener)

        listDialog.setTitle("Delete this list?")
            .setMessage("Deleting this list will also delete $amountOfTasks " +
                    "${if (amountOfTasks > 1) "tasks" else "task"}.")
            .setPositiveButton("Delete", listClickListener)
            .setNegativeButton("Cancel", listClickListener)

        binding.renameList.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToRenameListFragment(currentListId))
            this.dismiss()
        }

        binding.deleteList.setOnClickListener {
            listDialog.show()
            this.dismiss()
        }

        binding.deleteCompleted.setOnClickListener {
            stageDialog.show()
            this.dismiss()
        }

        return binding.root
    }
}