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

        val application = requireNotNull(this.activity).application
        val arguments = EditTaskFragmentArgs.fromBundle(requireArguments())

        // Create an instance of the ViewModel Factory.
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = EditTaskViewModelFactory(dataSource, arguments.taskKey)

        // Get a reference to the ViewModel associated with this fragment.
        val editTaskViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(EditTaskViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.

        binding.editTaskViewModel = editTaskViewModel

        binding.lifecycleOwner = this

        var date: LocalDate? = null

        editTaskViewModel.task.observe(this, Observer {
            date = it.date
        })

        binding.editDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(it.context, R.style.DatePickerTheme)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                date = LocalDate.of(year, month + 1, dayOfMonth)
                binding.editDate.setText(formatDate(date))
            }
            datePickerDialog.show()
        }

        binding.arrowBack.setOnClickListener {
            val title = binding.editTask.text.toString()
            val details = binding.addInfo.text.toString()

            editTaskViewModel.updateTask(title, details, date)
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
