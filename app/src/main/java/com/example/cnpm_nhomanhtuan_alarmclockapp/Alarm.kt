package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "label")
    val label: String="",
    @ColumnInfo(name = "time")
    val time: String="",  // Format: "HH:mm AM/PM"
    @ColumnInfo(name = "days")
    val days: List<String> = emptyList(),  // ["S", "M", "T", "W", "T", "F", "S"], empty string if day not selected
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = false
)