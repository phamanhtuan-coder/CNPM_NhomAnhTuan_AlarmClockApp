package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.app.Application
import androidx.work.Configuration
import android.util.Log
import androidx.work.WorkerFactory

class AlarmApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()

        // Log for debugging
        Log.d("AlarmApp", "Application initialized")
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)  // Set log level
            .build()
    }
}
