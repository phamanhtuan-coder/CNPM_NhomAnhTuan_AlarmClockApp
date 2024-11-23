package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.media.Ringtone
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view.AlarmRingScreen
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
                Log.d("AlarmRingActivity", "Alarm is enabled, starting custom sound.")
            }
        }

        setContent {
            AlarmRingScreen(
                alarmId = alarmId,
                getAlarmById = { id -> Graph.repository.getAlarmById(id) },
                onStopAlarm = { finish() }
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }

    private fun stopAlarm() {
        if (ringtone?.isPlaying == true) {
            ringtone?.stop()
        }
    }
}