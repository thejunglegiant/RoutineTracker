package com.oleksii.routinetracker.createtask

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
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

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = CreateTaskViewModelFactory(dataSource)

        // Get a reference to the ViewModel associated with this fragment.
        val createTaskViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(CreateTaskViewModel::class.java)

        var date: LocalDate? = null

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.createTaskViewModel = createTaskViewModel
        binding.setLifecycleOwner(this)

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