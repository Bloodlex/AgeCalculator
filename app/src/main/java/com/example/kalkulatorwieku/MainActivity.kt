package com.example.kalkulatorwieku

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import java.time.LocalDate
import java.time.Period

class MainActivity : ComponentActivity() {

    private lateinit var selectedDateTextView: TextView
    private lateinit var datePicker: DatePicker
    private lateinit var selectDateButton: Button
    private lateinit var ageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        enableEdgeToEdge()

        selectedDateTextView = findViewById(R.id.selectedDateTextView)
        selectDateButton = findViewById(R.id.button)
        datePicker = findViewById(R.id.datePicker)
        ageTextView = findViewById(R.id.ageTextView)

        // Calculate the default date (15 years ago + 1 day)
        val today = LocalDate.now()
        val defaultDate = today.minusYears(15).minusDays(1)
        val defaultYear = defaultDate.year
        val defaultMonth = defaultDate.monthValue - 1 // Month is 0-indexed in DatePicker
        val defaultDay = defaultDate.dayOfMonth

        // Set the initial date
        datePicker.updateDate(defaultYear, defaultMonth, defaultDay)

        // Hide the DatePicker initially
        datePicker.visibility = android.view.View.GONE

        selectDateButton.setOnClickListener {
            // Toggle the visibility of the DatePicker
            if (datePicker.visibility == android.view.View.GONE) {
                datePicker.visibility = android.view.View.VISIBLE
                selectDateButton.text = "Ukryj"
            } else {
                datePicker.visibility = android.view.View.GONE
                selectDateButton.text = "Wybierz datę"
            }
        }

        // Listen for date changes
        datePicker.setOnDateChangedListener { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            selectedDateTextView.text = "Minęło: $selectedDate"

            // Calculate and display the age
            calculateAndDisplayAge(selectedYear, selectedMonth, selectedDay)
        }
        // Calculate and display the age for the default date
        calculateAndDisplayAge(defaultYear, defaultMonth, defaultDay)
    }

    private fun calculateAndDisplayAge(year: Int, month: Int, day: Int) {
        val selectedDate = LocalDate.of(year, month + 1, day)
        val today = LocalDate.now()
        // Calculate the default date (15 years ago + 1 day)
        val defaultDate = today.minusYears(15).minusDays(1)
        // Calculate the difference between the selected date and today
        val period = Period.between(selectedDate, today)
        // Calculate the difference between the default date and today
        val periodDefault = Period.between(defaultDate, today)

        val years = period.years
        val months = period.months
        val days = period.days

        if (selectedDate == defaultDate) {
            val yearsDefault = periodDefault.years
            val monthsDefault = periodDefault.months
            val daysDefault = periodDefault.days
            val ageText = "Lat: $yearsDefault, Miesięcy: $monthsDefault, Dni: $daysDefault"
            ageTextView.text = ageText
        } else {
            val ageText = "Lat: $years, Miesięcy: $months, Dni: $days"
            ageTextView.text = ageText
        }
    }
}