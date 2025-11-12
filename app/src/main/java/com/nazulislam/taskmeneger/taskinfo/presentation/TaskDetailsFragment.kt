package com.nazulislam.taskmeneger.taskinfo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.nazulislam.taskmeneger.databinding.FragmentTaskDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * ..................Note..........................
 * The date picker function is created by ChatGpt
 */

class TaskDetailsFragment : Fragment() {
    // Use nullable backing property for view binding to avoid leaks
    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate using binding and return the root
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dueDate = binding.inTaskDueDate
        dueDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        // Prevent selecting previous dates by using DateValidatorPointForward with today's millis
        val start = MaterialDatePicker.todayInUtcMilliseconds()
        val validator = DateValidatorPointForward.from(start)
        val constraints = CalendarConstraints.Builder()
            .setValidator(validator)
            .build()

        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Due Date")
            .setSelection(start)
            .setCalendarConstraints(constraints)

        val datePicker = builder.build()

        // Add listener first, then show the picker
        datePicker.addOnPositiveButtonClickListener { selection ->
            // selection is the epoch millis (Long)
            val epoch = selection as? Long ?: return@addOnPositiveButtonClickListener
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.inTaskDueDate.setText(dateFormat.format(Date(epoch)))
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
