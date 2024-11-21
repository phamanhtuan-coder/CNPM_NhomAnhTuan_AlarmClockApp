package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AlarmRingActivity : AppCompatActivity() {

    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alarmId = intent.getIntExtra("ALARM_ID", -1)

        if (alarmId == -1) {
            Log.e("AlarmRingActivity", "Không tìm thấy ID báo thức!")
            finish()
            return
        }

        // Kiểm tra trạng thái của báo thức
        lifecycleScope.launch {
            val isEnabled = Graph.repository.isAlarmEnabled(alarmId)
            Log.d("AlarmRingActivity", "Alarm ID: $alarmId, is_enabled: $isEnabled")
            if (!isEnabled) {
                Log.d("AlarmRingActivity", "Alarm is not enabled, stopping activity.")
                finish()
            } else {
                Log.d("AlarmRingActivity", "Alarm is enabled, starting custom sound.")
            }
        }

        // Hiển thị giao diện với `AlarmRingScreen`
        setContent {
            AlarmRingScreen(
                alarmId = alarmId,
                onStopAlarm = {
                    finish() // Kết thúc Activity sau khi dừng báo thức
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm() // Đảm bảo dừng âm thanh khi Activity bị hủy
    }

    private fun stopAlarm() {
        if (ringtone?.isPlaying == true) {
            ringtone?.stop()
        }
    }
}
