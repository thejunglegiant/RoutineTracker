package com.oleksii.routinetracker

import java.time.LocalDate

fun formatDate(date: LocalDate): String {
    val month = when(date.month?.value) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Okt"
        11 -> "Nov"
        else -> "Dec"
    }
    val day = when(date.dayOfWeek.value) {
        1 -> "Mon"
        2 -> "Tue"
        3 -> "Wed"
        4 -> "Thu"
        5 -> "Fri"
        6 -> "Sat"
        else -> "Sun"
    }

    return if (date != LocalDate.MIN && date.isBefore(LocalDate.now())) {
        "Past"
    } else if (date >= LocalDate.now()) {
        "$day, ${date.dayOfMonth} $month"
    } else {
        "No due date"
    }
}