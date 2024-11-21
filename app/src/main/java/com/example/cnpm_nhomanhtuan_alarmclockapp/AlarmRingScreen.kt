package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarmapp.Alarm

@Composable
fun AlarmRingScreen(
    alarmId: Int,
    getAlarmById: (Int) -> kotlinx.coroutines.flow.Flow<Alarm>,
    onStopAlarm: () -> Unit
) {
    val context = LocalContext.current

    // Quan sát `Alarm` từ Flow
    val alarmFlow = getAlarmById(alarmId)
    val alarm by alarmFlow.collectAsState(initial = null)

    // Trạng thái MediaPlayer
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val soundResId = when (alarm?.sound) {
        "res/raw/nature_melody" -> R.raw.nature_melody
        "res/raw/morning_chime" -> R.raw.morning_chime
        else -> R.raw.morning_chime // Mặc định
    }
    Log.d("AlarmRingScreen", "Resolved soundResId directly: $soundResId")


    LaunchedEffect(soundResId) {
        mediaPlayer?.release()

        try {
            mediaPlayer = MediaPlayer.create(context, soundResId)?.apply {
                isLooping = true
                start() // Bắt đầu phát âm thanh
                Log.d("AlarmRingScreen", "MediaPlayer started successfully with soundResId: $soundResId")
            }
        } catch (e: Exception) {
            Log.e("AlarmRingScreen", "Error initializing MediaPlayer: ${e.message}")
        }
    }



    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text = "Đang phát âm thanh: ${alarm?.sound ?: "Mặc định"}",
//                fontSize = 18.sp,
//                color = MaterialTheme.colorScheme.primary
//            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Báo thức đang kêu!",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    try {
                        mediaPlayer?.let {
                            if (it.isPlaying) {
                                it.stop()
                            }
                            it.release()
                        }
                        mediaPlayer = null
                        onStopAlarm()
                    } catch (e: Exception) {
                        Log.e("AlarmRingScreen", "Error stopping MediaPlayer: ${e.message}")
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(text = "Dừng báo thức", fontSize = 16.sp)
            }
        }
    }

    // Dừng MediaPlayer khi giao diện bị hủy
    DisposableEffect(Unit) {
        onDispose {
            try {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        it.stop()
                    }
                    it.release()
                }
                mediaPlayer = null
            } catch (e: Exception) {
                Log.e("AlarmRingScreen", "Error releasing MediaPlayer: ${e.message}")
            }
        }
    }
}

