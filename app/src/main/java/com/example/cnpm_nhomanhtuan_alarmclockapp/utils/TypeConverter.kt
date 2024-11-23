package com.example.cnpm_nhomanhtuan_alarmclockapp.utils

import androidx.room.TypeConverter
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Time


class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromTime(value: Time): String {
        return "${value.hour},${value.minute},${value.amPm}"
    }

    @TypeConverter
    fun toTime(value: String): Time {
        val list = value.split(",")
        return if (list.size == 3) {
            Time(list[0].toInt(), list[1].toInt(), list[2])
        } else {
            Time(0, 0, "AM") // Giá trị mặc định nếu lỗi
        }
    }

}