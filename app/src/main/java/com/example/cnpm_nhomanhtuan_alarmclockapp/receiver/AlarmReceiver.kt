package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view.AlarmRingActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)

        if (alarmId == -1) {
            Log.e("AlarmReceiver", "Không tìm thấy ID báo thức!")
            return
        }

        Log.d("AlarmReceiver", "Báo thức được kích hoạt với ID: $alarmId")

        val ringIntent = Intent(context, AlarmRingActivity::class.java).apply {
            putExtra("ALARM_ID", alarmId) // Truyền ID báo thức
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Đảm bảo Activity được khởi chạy
        }
        context.startActivity(ringIntent)
    }
}