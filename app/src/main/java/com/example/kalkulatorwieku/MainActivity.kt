package com.example.kalkulatorwieku

import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.kalkulatorwieku.other.TimeHelper.Companion.calculateTimePassed
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
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

        setDefaultForDatePicker(hardLimitDefaultDate)
        setOnDateChangedListener()
        setDatesForTextfields(hardLimitDefaultDate)
    }

    private fun setDefaultForDatePicker(hardLimitDefaultDate: LocalDate?) {
        datePicker.maxDate = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (hardLimitDefaultDate != null) {
            datePicker.maxDate = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            datePicker.updateDate(
                hardLimitDefaultDate.year,
                hardLimitDefaultDate.monthValue - 1,
                hardLimitDefaultDate.dayOfMonth
            )
        }
    }

    private fun setDatesForTextfields(hardLimitDefaultDate: LocalDate?) {
        if (hardLimitDefaultDate != null) {
            calculateAndDisplayTimePassedTextField(
                hardLimitDefaultDate.year,
                hardLimitDefaultDate.monthValue,
                hardLimitDefaultDate.dayOfMonth
            )
        }
    }

    private fun setOnDateChangedListener() {
        datePicker.setOnDateChangedListener { _, selectedYear, selectedMonth, selectedDay ->
            calculateAndDisplayTimePassedTextField(selectedYear, selectedMonth + 1, selectedDay)
        }
    }

    private fun calculateAndDisplayTimePassedTextField(year: Int, month: Int, day: Int) {
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

    private fun findElements() {
        datePicker = findViewById(R.id.datePicker)
        ageTextView = findViewById(R.id.timePassedTextView)
        birthDateFor15YearsOldTextView = findViewById(R.id.birthDateFor15YearsOldTextView)
        birthDateFor18YearsOldTextView = findViewById(R.id.birthDateFor18YearsOldTextView)
    }
}
