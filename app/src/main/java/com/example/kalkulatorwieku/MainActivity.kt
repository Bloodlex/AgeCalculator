package com.example.kalkulatorwieku

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.time.LocalDate
import java.time.Period

class MainActivity : ComponentActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var selectDateButton: Button
    private lateinit var ageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        selectDateButton = findViewById(R.id.chooseBirthdateButton)
        datePicker = findViewById(R.id.datePicker)
        ageTextView = findViewById(R.id.timePassedTextView)

        val defaultDate = calculateHardLimit()

        if (defaultDate != null) {
            datePicker.updateDate(defaultDate.year, defaultDate.monthValue - 1, defaultDate.dayOfMonth)
        }

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
            calculateAndDisplayAge(selectedYear, selectedMonth, selectedDay)
        }

        // Calculate and display the age for the default date
        if (defaultDate != null) {
            calculateAndDisplayAge(defaultDate.year, defaultDate.monthValue - 1, defaultDate.dayOfMonth)
        }
    }

    private fun calculateAndDisplayAge(year: Int, month: Int, day: Int) {
        val selectedDate = LocalDate.of(year, month + 1, day)
        val today = LocalDate.now()

        val defaultDate = calculateHardLimit()
        val periodSelectedDateToToday = Period.between(selectedDate, today)
        val periodDefaultDateToToday = Period.between(defaultDate, today)

        val years = periodSelectedDateToToday.years
        val months = periodSelectedDateToToday.months
        val days = periodSelectedDateToToday.days

        if (selectedDate == defaultDate) {
            val yearsDefault = periodDefaultDateToToday.years
            val monthsDefault = periodDefaultDateToToday.months
            val daysDefault = periodDefaultDateToToday.days
            val ageText = "Lat: $yearsDefault, Miesięcy: $monthsDefault, Dni: $daysDefault"
            ageTextView.text = ageText
        } else {
            val ageText = "Lat: $years, Miesięcy: $months, Dni: $days"
            ageTextView.text = ageText
        }
    }

    private fun calculateHardLimit(): LocalDate? {
        return calculateTimePassed(15)
    }

    private fun calculateSoftLimit(): LocalDate? {
        return calculateTimePassed(18)
    }

    private fun calculateTimePassed(years: Long): LocalDate? {
        val today = LocalDate.now()
        val defaultDate = today.minusYears(years).minusDays(1)
        return defaultDate
    }
}