package com.example.cnpm_nhomanhtuan_alarmclockapp.di

import android.content.Context
import android.util.Log
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.source.local.AlarmDatabase
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.repository.AlarmRepository

object Graph {
    private lateinit var db: AlarmDatabase

    // Đảm bảo repository được khởi tạo đúng cách
    val repository by lazy {
        AlarmRepository(db.alarmDao)
    }

    fun provide(context: Context) {
        db = AlarmDatabase.getInstance(context)
        Log.d("Graph", "Database initialized")
    }


}