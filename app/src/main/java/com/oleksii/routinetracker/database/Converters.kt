package com.oleksii.routinetracker.database

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun toLocalDate(date: LocalDate): String {
        return date.toString()
    }
}