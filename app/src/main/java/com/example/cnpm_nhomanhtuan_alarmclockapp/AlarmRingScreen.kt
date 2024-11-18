package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmRingScreen(alarmId: Int, onStopAlarm: () -> Unit) {
    val context = LocalContext.current

    // Trạng thái MediaPlayer
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.morning_chime) }

    // Khởi động phát âm thanh khi giao diện được dựng
    LaunchedEffect(Unit) {
        mediaPlayer.isLooping = true // Lặp lại âm thanh
        mediaPlayer.start()
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
            Text(
                text = "Báo thức đang kêu!",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = "ID báo thức: $alarmId",
//                fontSize = 18.sp,
//                color = MaterialTheme.colorScheme.secondary
//            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    mediaPlayer.stop() // Dừng âm thanh
                    mediaPlayer.release() // Giải phóng MediaPlayer
                    onStopAlarm() // Gọi hàm callback
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(text = "Dừng báo thức", fontSize = 16.sp)
            }
        }
    }

    // Dừng âm thanh khi giao diện bị hủy
    DisposableEffect(Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer.release()
        }
    }
}
