package com.oleksii.routinetracker.edittask


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.text.set
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentEditTaskBinding
import com.oleksii.routinetracker.formatDate
import com.oleksii.routinetracker.hideKeyboard
import java.time.LocalDate

class EditTaskFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentEditTaskBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_task, container, false)

        // Create an instance of the ViewModel.
        val application = requireNotNull(this.activity).application
        val arguments = EditTaskFragmentArgs.fromBundle(requireArguments())
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val editTaskViewModel = EditTaskViewModel(dataSource, arguments.taskKey)
        binding.editTaskViewModel = editTaskViewModel

        var taskIsNotCompleted: Boolean = true
        var date: LocalDate = LocalDate.MIN
        var stage = 0

        binding.lifecycleOwner = this

        binding.editDate.setOnClickListener {
            if (taskIsNotCompleted) {
                val datePickerDialog = DatePickerDialog(it.context, R.style.DatePickerTheme)
                datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                    date = LocalDate.of(year, month + 1, dayOfMonth)
                    binding.editDate.setText(formatDate(date))
                }
                datePickerDialog.show()
            }
        }

        editTaskViewModel.task.observe(this, Observer {
            date = it.date
            if (it.stage == 1) {
                taskIsNotCompleted = false
                binding.addDetails.keyListener = null
                binding.addDetails.isEnabled = false
                binding.editTask.keyListener = null
                stage = 1
            }
        })


        binding.arrowBack.setOnClickListener {
            val title = binding.editTask.text.toString()
            var details = binding.addDetails.text.toString()
            if (binding.addDetails.text.isEmpty())
                details = ""

            editTaskViewModel.updateTask(title, details, date, stage)
            this.findNavController().navigate(R.id.action_editTaskFragment_to_listFragment)
            hideKeyboard(activity)
        }

        binding.deleteTask.setOnClickListener {
            editTaskViewModel.deleteTask()
            this.findNavController().navigate(R.id.action_editTaskFragment_to_listFragment)
        }

        return binding.root
    }


}
