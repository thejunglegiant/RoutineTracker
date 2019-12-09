package com.oleksii.routinetracker.edittask


import android.app.Application
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.createtask.CreateTaskViewModel
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentCreateTaskBinding
import com.oleksii.routinetracker.databinding.FragmentEditTaskBinding
import com.oleksii.routinetracker.formatDate
import com.oleksii.routinetracker.hideKeyboard
import com.oleksii.routinetracker.list.ListFragment
import java.time.LocalDate

class EditTaskFragment : Fragment() {

    lateinit var binding: FragmentEditTaskBinding
    private lateinit var editTaskViewModel: EditTaskViewModel
    var date: LocalDate = LocalDate.MIN
    var saveButtonWasNotPressed = true
    val currentListId = ListFragment.currentListId
    var stage = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_task, container, false)
        val application: Application = requireNotNull(this.activity).application
        val arguments = EditTaskFragmentArgs.fromBundle(requireArguments())
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        editTaskViewModel = EditTaskViewModel(dataSource, arguments.taskKey)
        binding.editTaskViewModel = editTaskViewModel
        binding.lifecycleOwner = this
        var taskIsNotCompleted = true

        binding.editDate.setOnClickListener {
            if (taskIsNotCompleted) {
                val datePickerDialog = DatePickerDialog(it.context, R.style.DatePickerTheme)
                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
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
                binding.editTitle.keyListener = null
                stage = 1
            }
        })


        binding.arrowBack.setOnClickListener {
            savingTask()
            saveButtonWasNotPressed = false
            this.findNavController().navigate(R.id.action_editTaskFragment_to_listFragment)
        }

        binding.deleteTask.setOnClickListener {
            editTaskViewModel.deleteTask()
            this.findNavController().navigate(R.id.action_editTaskFragment_to_listFragment)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (saveButtonWasNotPressed)
            savingTask()
    }

    private fun savingTask() {
        val title = binding.editTitle.text.toString()
        var details = binding.addDetails.text.toString()
        if (binding.addDetails.text.isEmpty())
            details = ""
        if (title.isNotEmpty()) {
            editTaskViewModel.updateTask(currentListId, title, details, date, stage)
            hideKeyboard(activity)
        }  else
            Snackbar.make(binding.newTaskLayout,
                this.getString(R.string.enter_list_title), Snackbar.LENGTH_SHORT).show()
    }

}
