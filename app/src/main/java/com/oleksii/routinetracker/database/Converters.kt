package com.oleksii.routinetracker.database

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(value: String?): LocalDate? {
        return when (value) {
            null -> null
            else -> LocalDate.parse(value)
        }
    }

    @TypeConverter
    fun toLocalDate(date: LocalDate?): String? {
        return when (date) {
            null -> null
            else -> date.toString()
        }
    }
}