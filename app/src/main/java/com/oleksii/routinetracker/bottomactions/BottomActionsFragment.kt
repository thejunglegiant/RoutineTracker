package com.oleksii.routinetracker.bottomactions

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentBottomActionsBinding
import com.oleksii.routinetracker.list.ListFragment
import com.oleksii.routinetracker.list.ListFragmentDirections

class BottomActionsFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentBottomActionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bottom_actions, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val bottomActionsViewModel = BottomActionsViewModel(dataSource)
        val amountOfCompletedTasks = ListFragment.amountOfCompletedTasks
        val amountOfTasks = ListFragment.amountOfTasks
        val currentListId = ListFragment.currentListId

        val stageDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        val listDialog: AlertDialog.Builder = AlertDialog.Builder(context)

        val stageClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (amountOfCompletedTasks != 0) {
                            bottomActionsViewModel.deleteAllCompletedTasks()
                            ListFragment.showSnackBar("Completed tasks were deleted")
                        }
                    }
                }
            }

        val listClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        bottomActionsViewModel.deleteCurrentList(currentListId)
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

        if (currentListId == 1.toLong()) {
            binding.deleteList.isEnabled = false
            binding.deleteList.setTextColor(
                ContextCompat.getColor(application.applicationContext, (R.color.textHintColor))
            )
            binding.deleteListHint.visibility = View.VISIBLE
        }

        if (amountOfCompletedTasks < 1) {
            binding.deleteCompleted.isEnabled = false
            binding.deleteCompleted.setTextColor(
                ContextCompat.getColor(application.applicationContext, (R.color.textHintColor))
            )
            binding.deleteCompletedHint.visibility = View.VISIBLE
        }

        binding.renameList.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToRenameListFragment(currentListId))
            this.dismiss()
        }

        binding.deleteList.setOnClickListener {
            if (amountOfTasks < 1) {
                bottomActionsViewModel.deleteCurrentList(currentListId)
                ListFragment.showSnackBar("List were successfully deleted")
            } else {
                listDialog.show()
            }
            this.dismiss()
        }

        binding.deleteCompleted.setOnClickListener {
            stageDialog.show()
            this.dismiss()
        }

        return binding.root
    }
}