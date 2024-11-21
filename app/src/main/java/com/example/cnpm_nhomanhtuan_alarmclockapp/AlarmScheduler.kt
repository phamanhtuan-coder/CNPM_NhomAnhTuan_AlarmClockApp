package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alarmapp.Alarm
import java.util.Calendar

object AlarmScheduler {

    fun getDayOfWeekFromChar(dayChar: String, position: Int): Int {
        return when (dayChar) {
            "S" -> if (position == 0) Calendar.SUNDAY else Calendar.SATURDAY
            "M" -> Calendar.MONDAY
            "T" -> if (position == 2) Calendar.TUESDAY else Calendar.THURSDAY
            "W" -> Calendar.WEDNESDAY
            "F" -> Calendar.FRIDAY
            else -> throw IllegalArgumentException("Invalid day character: $dayChar")
        }
    }

    private fun convertTo24HourFormat(hour: Int, isPM: String): Int {
        return if (isPM == "PM" && hour < 12) {
            hour + 12
        } else if (isPM == "AM" && hour == 12) {
            0
        } else {
            hour
        }
    }

    fun scheduleAlarm(context: Context, alarmId: Int, hour: Int, minute: Int, dayOfWeek: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarmId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId + dayOfWeek,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        Log.d("AlarmScheduler", "PendingIntent created for alarm ID: $alarmId")
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= Calendar.getInstance().timeInMillis) {
                add(Calendar.WEEK_OF_MONTH, 1)
            }
        }

        println("Scheduling alarm for ID: $alarmId at ${calendar.time} for day: $dayOfWeek")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        (0..6).forEach { dayOffset ->
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId + dayOffset,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    fun scheduleAlarmIfEnabled(context: Context, alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (alarm.isEnabled) {
            if (alarm.time.amPm != "AM" && alarm.time.amPm != "PM") {
                throw IllegalArgumentException("Invalid AM/PM format: ${alarm.time.amPm}")
            }

            val hour24Format = convertTo24HourFormat(alarm.time.hour, alarm.time.amPm)
            if (alarm.days.all { it.isEmpty() }) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour24Format)
                    set(Calendar.MINUTE, alarm.time.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    if (timeInMillis <= Calendar.getInstance().timeInMillis) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }

                println("Scheduling one-time alarm for ID: ${alarm.id} at ${calendar.time}")

                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("ALARM_ID", alarm.id)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarm.id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarm.days.forEachIndexed { index, dayChar ->
                    if (dayChar.isNotEmpty()) {
                        try {
                            val dayOfWeek = getDayOfWeekFromChar(dayChar, index)
                            scheduleAlarm(context, alarm.id, hour24Format, alarm.time.minute, dayOfWeek)
                        } catch (e: IllegalArgumentException) {
                            println("Error scheduling alarm: ${e.message}")
                        }
                    }
                }
            }
        } else {
            cancelAlarm(context, alarm.id)
        }
    }
}
