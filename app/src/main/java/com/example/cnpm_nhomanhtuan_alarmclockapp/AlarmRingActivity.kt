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

        lifecycleScope.launch {
            val isEnabled = Graph.repository.isAlarmEnabled(alarmId)
            Log.d("AlarmRingActivity", "Alarm ID: $alarmId, is_enabled: $isEnabled")
            if (!isEnabled) {
                Log.d("AlarmRingActivity", "Alarm is not enabled, stopping activity.")
                finish()
            } else {
                Log.d("AlarmRingActivity", "Alarm is enabled, starting alarm.")
                // Tiếp tục logic
                val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
                ringtone?.play()
            }
        }



        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
        ringtone?.play()


        if (alarmId == -1) {
            Log.e("AlarmRingActivity", "Không tìm thấy ID báo thức!")
            finish()
            return
        }

        Log.d("AlarmRingActivity", "Báo thức được kích hoạt với ID: $alarmId")

        // Phát âm báo thức
//        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//        ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
//        ringtone?.play()

        // Hiển thị giao diện Compose
        setContent {
            AlarmRingScreen(
                alarmId = alarmId, // Sử dụng ID báo thức từ Intent
                onStopAlarm = {
                    stopAlarm() // Dừng báo thức
                    finish() // Kết thúc Activity
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
