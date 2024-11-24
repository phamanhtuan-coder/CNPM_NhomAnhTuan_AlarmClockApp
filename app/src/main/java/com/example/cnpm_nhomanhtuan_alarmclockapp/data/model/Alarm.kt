package com.example.cnpm_nhomanhtuan_alarmclockapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cnpm_nhomanhtuan_alarmclockapp.utils.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "label") val label: String = "",
    @ColumnInfo(name = "time") val time: Time = Time(0, 0, "AM"),
    @ColumnInfo(name = "days") val days: List<String> = emptyList(),
    @ColumnInfo(name = "is_enabled") val isEnabled: Boolean = false,
    @ColumnInfo(name = "sound") val sound: String = "R.raw.morning_chime"
)

data class Time(var hour: Int, var minute: Int, var amPm: String)