package com.example.cnpm_nhomanhtuan_alarmclockapp


import android.content.Context

object Graph {
    lateinit var db: AlarmDatabase
        private set
    val repository by lazy {
        AlarmRepository(db.alarmDao)
    }

    fun provideDatabase(context: Context) {
        db = AlarmDatabase.getDatabase(context)
    }
}