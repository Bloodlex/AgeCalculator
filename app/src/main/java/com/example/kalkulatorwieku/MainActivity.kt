package com.example.kalkulatorwieku

import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

private val DEFAULT_DATE_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy")

private const val HARD_LIMIT_AGE: Long = 15L
private const val SOFT_LIMIT_AGE: Long = 18L

class MainActivity : ComponentActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var ageTextView: TextView
    private lateinit var birthDateFor15YearsOldTextView: TextView
    private lateinit var birthDateFor18YearsOldTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        findElements()

        val hardLimitDefaultDate = calculateHardLimit()
        val calculateSoftLimit = calculateSoftLimit()

        if (hardLimitDefaultDate != null && calculateSoftLimit != null) {
            birthDateFor15YearsOldTextView.text = hardLimitDefaultDate.format(DEFAULT_DATE_PATTERN)
            birthDateFor18YearsOldTextView.text = calculateSoftLimit.format(DEFAULT_DATE_PATTERN)
        }

        if (hardLimitDefaultDate != null) {
            datePicker.updateDate(
                hardLimitDefaultDate.year,
                hardLimitDefaultDate.monthValue,
                hardLimitDefaultDate.dayOfMonth
            )
        }

        setOnDateChangedListener()
        setDefaultDate(hardLimitDefaultDate)
    }

    private fun setDefaultDate(hardLimitDefaultDate: LocalDate?) {
        if (hardLimitDefaultDate != null) {
            calculateAndDisplayAge(
                hardLimitDefaultDate.year,
                hardLimitDefaultDate.monthValue,
                hardLimitDefaultDate.dayOfMonth
            )
        }
    }

    private fun setOnDateChangedListener() {
        datePicker.setOnDateChangedListener { _, selectedYear, selectedMonth, selectedDay ->
            calculateAndDisplayAge(selectedYear, selectedMonth, selectedDay)
        }
    }

    private fun calculateAndDisplayAge(year: Int, month: Int, day: Int) {
        val selectedDate = LocalDate.of(year, month, day)
        val today = LocalDate.now()

        val hardLimitDefaultDate = calculateHardLimit()
        val periodSelectedDateToToday = Period.between(selectedDate, today)
        val periodDefaultDateToToday = Period.between(hardLimitDefaultDate, today)

        val ageText: String = if (selectedDate == hardLimitDefaultDate) {
            getString(
                R.string.duration_format, periodDefaultDateToToday.years,
                periodDefaultDateToToday.months, periodDefaultDateToToday.days
            )
        } else {
            getString(
                R.string.duration_format, periodSelectedDateToToday.years,
                periodSelectedDateToToday.months, periodSelectedDateToToday.days
            )
        }

        ageTextView.text = ageText
    }

    private fun calculateHardLimit(): LocalDate? {
        return calculateTimePassed(HARD_LIMIT_AGE)
    }

    private fun calculateSoftLimit(): LocalDate? {
        return calculateTimePassed(SOFT_LIMIT_AGE)
    }

    private fun calculateTimePassed(years: Long): LocalDate? {
        val today = LocalDate.now()
        val defaultDate = today.minusYears(years).minusDays(1)
        return defaultDate
    }

    private fun findElements() {
        datePicker = findViewById(R.id.datePicker)
        ageTextView = findViewById(R.id.timePassedTextView)
        birthDateFor15YearsOldTextView = findViewById(R.id.birthDateFor15YearsOldTextView)
        birthDateFor18YearsOldTextView = findViewById(R.id.birthDateFor18YearsOldTextView)
    }
}
