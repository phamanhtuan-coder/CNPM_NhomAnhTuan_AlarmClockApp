package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmRingScreen(alarmId: Int, onStopAlarm: () -> Unit) {
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ID báo thức: $alarmId",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStopAlarm,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(text = "Dừng báo thức", fontSize = 16.sp)
            }
        }
    }
}
