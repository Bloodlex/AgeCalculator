package com.example.kalkulatorwieku

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var selectDateButton: Button
    private lateinit var ageTextView: TextView
    private lateinit var birthDateFor15YearsOldTextView: TextView
    private lateinit var birthDateFor18YearsOldTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        selectDateButton = findViewById(R.id.chooseBirthdateButton)
        datePicker = findViewById(R.id.datePicker)
        ageTextView = findViewById(R.id.timePassedTextView)
        birthDateFor15YearsOldTextView = findViewById(R.id.birthDateFor15YearsOldTextView)
        birthDateFor18YearsOldTextView = findViewById(R.id.birthDateFor18YearsOldTextView)

        val defaultDate = calculateHardLimit()
        val ofPattern = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val calculateSoftLimit = calculateSoftLimit()
        if (defaultDate != null && calculateSoftLimit != null) {
            birthDateFor15YearsOldTextView.text = defaultDate.format(ofPattern)
            birthDateFor18YearsOldTextView.text = calculateSoftLimit.format(ofPattern);
        }

        if (defaultDate != null) {
            datePicker.updateDate(
                defaultDate.year,
                defaultDate.monthValue - 1,
                defaultDate.dayOfMonth
            )
        }

        // Hide the DatePicker initially
        datePicker.visibility = android.view.View.GONE

        selectDateButton.setOnClickListener {
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
            calculateAndDisplayAge(
                defaultDate.year,
                defaultDate.monthValue - 1,
                defaultDate.dayOfMonth
            )
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

    /*    private fun LocalDate.formatDate(): String {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
            return format(formatter)
        }*/
}