package com.oleksii.routinetracker

import java.time.LocalDate

fun formatDate(date: LocalDate?): String {
    val currentDate: LocalDate = LocalDate.now()
    val month: String
    val day: String
    when(date?.dayOfWeek?.value) {
        1 -> day = "Mon"
        2 -> day = "Tue"
        3 -> day = "Wed"
        4 -> day = "Thu"
        5 -> day = "Fri"
        6 -> day = "Sat"
        7 -> day = "Sun"
        else -> day = "No"
    }
    when(date?.month?.value) {
        1 -> month = "Jan"
        2 -> month = "Feb"
        3 -> month = "Mar"
        4 -> month = "Apr"
        5 -> month = "May"
        6 -> month = "Jun"
        7 -> month = "Jul"
        8 -> month = "Aug"
        9 -> month = "Sep"
        10 -> month = "Okt"
        11 -> month = "Nov"
        12 -> month = "Dec"
        else -> month = "due date"
    }
    if (date != null && date.dayOfMonth < currentDate.dayOfMonth) {
        return "Past"
    } else {
        return "$day, ${if (date?.dayOfMonth==null) "" else date.dayOfMonth} $month"
    }
}