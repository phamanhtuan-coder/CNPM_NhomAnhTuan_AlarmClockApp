package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.content.Context
import android.util.Log

object Graph {
    lateinit var db: AlarmDatabase

    // Đảm bảo repository được khởi tạo đúng cách
    val repository by lazy {
        AlarmRepository(db.alarmDao)
    }

    fun provide(context: Context) {
        db = AlarmDatabase.getInstance(context)
        Log.d("Graph", "Database initialized")
    }


}
