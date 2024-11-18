package com.example.alarmapp

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cnpm_nhomanhtuan_alarmclockapp.AlarmRingActivity


class AlarmWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
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
