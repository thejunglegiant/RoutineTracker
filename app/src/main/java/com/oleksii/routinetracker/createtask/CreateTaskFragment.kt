package com.oleksii.routinetracker.createtask

import android.app.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.routinetracker.R
import com.oleksii.routinetracker.database.TaskDatabase
import com.oleksii.routinetracker.databinding.FragmentBottomSheetBinding
import com.oleksii.routinetracker.focusAndShowKeyboard
import com.oleksii.routinetracker.formatDate
import java.time.LocalDate

class CreateTaskFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentBottomSheetBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bottom_sheet, container, false)

        // Create an instance of the ViewModel.
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val createTaskViewModel = CreateTaskViewModel(dataSource)
        binding.createTaskViewModel = createTaskViewModel

        // For datePicker
        var date: LocalDate = LocalDate.MIN

        binding.lifecycleOwner = this

        // Set the focus to the edit text.
        binding.editTitle.requestFocus()

        binding.detailsAdd.setOnClickListener {
            binding.addDetails.visibility = View.VISIBLE
            focusAndShowKeyboard(activity, binding.addDetails)
        }

        binding.datePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(it.context, R.style.DatePickerTheme)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                date = LocalDate.of(year, month + 1, dayOfMonth)
                binding.dateShow.setText(formatDate(date))
                binding.dateShow.visibility = View.VISIBLE
            }
            datePickerDialog.show()
        }

        binding.saveButton.setOnClickListener {
            if (binding.editTitle.text.toString().trim().isNotEmpty()) {
                val title = binding.editTitle.text.toString()
                val details = binding.addDetails.text.toString()
                createTaskViewModel.createTask(title, details, date)
                this.dismiss()
            } else {
                Toast.makeText(context, "Enter the task!", Toast.LENGTH_SHORT ).show()
            }
        }

        return binding.root
    }

    // keyboard always under fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }
}