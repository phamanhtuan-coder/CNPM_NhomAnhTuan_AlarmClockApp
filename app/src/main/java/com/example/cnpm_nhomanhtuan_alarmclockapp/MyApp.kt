package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.cnpm_nhomanhtuan_alarmclockapp.di.Graph

class AlarmApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this) // Khởi tạo cơ sở dữ liệu khi ứng dụng bắt đầu
        Log.d("AlarmApp", "Application initialized")
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
    }
}