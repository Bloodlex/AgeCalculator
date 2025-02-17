package com.example.kalkulatorwieku.other

import java.time.LocalDate

class TimeHelper {

    companion object {
        fun calculateTimePassed(years: Long): LocalDate? {
            val today = LocalDate.now()
            val defaultDate = today.minusYears(years)
            return defaultDate
        }
    }
}