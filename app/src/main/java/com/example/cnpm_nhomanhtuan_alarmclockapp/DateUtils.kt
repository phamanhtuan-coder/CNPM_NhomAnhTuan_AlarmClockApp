package com.example.cnpm_nhomanhtuan_alarmclockapp

import java.util.Calendar

fun mapDayToCalendar(day: String): Int {
    return when (day) {
        "Sun" -> Calendar.SUNDAY
        "Mon" -> Calendar.MONDAY
        "Tue" -> Calendar.TUESDAY
        "Wed" -> Calendar.WEDNESDAY
        "Thu" -> Calendar.THURSDAY
        "Fri" -> Calendar.FRIDAY
        "Sat" -> Calendar.SATURDAY
        else -> throw IllegalArgumentException("Invalid day: $day")
    }
}
