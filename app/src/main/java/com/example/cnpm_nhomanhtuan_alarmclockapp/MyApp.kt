package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.app.Application
import androidx.work.Configuration

class MyApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this) // Khởi tạo cơ sở dữ liệu khi ứng dụng bắt đầu
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
    }
}
