package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit


class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Khởi động WorkManager để lên lịch báo thức
        val workRequest = PeriodicWorkRequest.Builder(AlarmWorker::class.java, 15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
        return START_STICKY
    }
}

class AlarmWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val alarmId = inputData.getInt("ALARM_ID", -1)
        val scheduledTime = inputData.getLong("SCHEDULED_TIME", 0)

        Log.d("AlarmWorker", "Worker triggered for alarm ID: $alarmId")
        Log.d("AlarmWorker", "Scheduled time: ${java.util.Date(scheduledTime)}")

        if (alarmId != -1) {
            val intent = Intent(applicationContext, AlarmRingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("ALARM_ID", alarmId)
                putExtra("SCHEDULED_TIME", scheduledTime)
            }
            applicationContext.startActivity(intent)
        }
        return Result.success()
    }
}

