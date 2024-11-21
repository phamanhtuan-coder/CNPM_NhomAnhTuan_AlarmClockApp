package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Khởi động lại dịch vụ nền khi thiết bị khởi động lại
            val serviceIntent = Intent(context, AlarmService::class.java)
            context.startService(serviceIntent)
            Toast.makeText(context, "Alarm Service Started", Toast.LENGTH_SHORT).show()
        }
    }
}
