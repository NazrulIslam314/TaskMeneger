package com.nazulislam.taskmeneger.utils

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

fun showDatePickerDialog(
    fragmentManager: FragmentManager, onDateSelected: (Long) -> Unit
) {
    val constraintsBuilder =
        CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

    val datePickerBuilder = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
        .setCalendarConstraints(constraintsBuilder.build())

    val datePicker = datePickerBuilder.build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        onDateSelected(selection)
    }

    datePicker.show(fragmentManager, "MATERIAL_DATE_PICKER")
}

