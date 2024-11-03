package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val time: String,  // Format: "HH:mm AM/PM"
    val days: List<String>,  // ["S", "M", "T", "W", "T", "F", "S"], empty string if day not selected
    val isEnabled: Boolean
)