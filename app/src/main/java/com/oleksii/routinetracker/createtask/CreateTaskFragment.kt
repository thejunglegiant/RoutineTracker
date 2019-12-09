package com.oleksii.routinetracker.createtask

import android.app.*
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentCreateTaskBinding
import com.oleksii.routinetracker.focusAndShowKeyboard
import com.oleksii.routinetracker.formatDate
import com.oleksii.routinetracker.hideKeyboard
import java.time.LocalDate
import com.oleksii.routinetracker.list.ListFragment.Companion.currentListId

class CreateTaskFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentCreateTaskBinding
    private lateinit var createTaskViewModel: CreateTaskViewModel
    var date: LocalDate = LocalDate.MIN
    private var saveButtonWasNotPressed = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_task, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        createTaskViewModel = CreateTaskViewModel(dataSource)
        binding.createTaskViewModel = createTaskViewModel
        binding.lifecycleOwner = this
        binding.editTitle.requestFocus()

        binding.addDetails.setOnClickListener {
            if (binding.editDetails.visibility == View.VISIBLE) {
                binding.editDetails.visibility = View.GONE
                hideKeyboard(activity)

            } else {
                binding.editDetails.visibility = View.VISIBLE
                focusAndShowKeyboard(activity, binding.editDetails)
            }
        }

        binding.datePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(it.context, R.style.DatePickerTheme)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                date = LocalDate.of(year, month + 1, dayOfMonth)
                binding.dateShow.text = formatDate(date)
                binding.dateShow.visibility = View.VISIBLE
            }
            datePickerDialog.show()
        }

        binding.dateShow.setOnClickListener {
            date = LocalDate.MIN
            it.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener {
            saveButtonWasNotPressed = false
            savingTask()
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (saveButtonWasNotPressed)
            savingTask()
    }

    private fun savingTask() {
        if (binding.editTitle.text.toString().trim().isNotEmpty()) {
            val title = binding.editTitle.text.toString()
            var details = binding.editDetails.text.toString()
            if (binding.editDetails.text.isEmpty())
                details = ""
            createTaskViewModel.createTask(currentListId, title, details, date)
        }
        this.dismiss()
    }
}