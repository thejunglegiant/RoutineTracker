package com.oleksii.routinetracker.bottommenu

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentBottomMenuBinding
import com.oleksii.routinetracker.list.ListFragment


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
        binding.buttomMenuViewModel= bottomMenuViewModel

        binding.lifecycleOwner = this

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (ListFragment.amountOfCompletedTasks != 0) {
                            bottomMenuViewModel.deleteAllCompletedTasks()
                            ListFragment.showSnackBar("Completed tasks were deleted")
                        }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        // nothing for now
                    }
                }
            }

        builder.setTitle("Delete all completed tasks?")
            .setMessage("There are ${ListFragment.amountOfCompletedTasks} " +
                "completed tasks that will be permanently removed.")
            .setPositiveButton("Delete", dialogClickListener)
            .setNegativeButton("Cancel", dialogClickListener)

        binding.deleteCompleted.setOnClickListener {
            builder.show()
            this.dismiss()
        }

        return binding.root
    }
}