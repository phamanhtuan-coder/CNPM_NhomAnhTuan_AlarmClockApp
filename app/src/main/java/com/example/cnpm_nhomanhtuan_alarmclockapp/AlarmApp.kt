package com.example.cnpm_nhomanhtuan_alarmclockapp


import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provideDatabase(this)
    }
}