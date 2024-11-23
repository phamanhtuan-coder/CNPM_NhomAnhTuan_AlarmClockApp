package com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view

import android.media.MediaPlayer
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cnpm_nhomanhtuan_alarmclockapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundPickerScreen(
    currentSound: String,
    navController: NavHostController,
    onSoundSelected: (String) -> Unit,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    val availableSounds = listOf(
        "Morning Chime",
        "Nature Melody"
    )
    var selectedSound by remember { mutableStateOf(currentSound) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = { Text("Chọn âm thanh", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 16.dp, horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancel) {
                        Text(
                            text = "Cancel",
                            color = Color.Yellow,
                            fontSize = 16.sp
                        )
                    }
                    TextButton(onClick = { onSave(selectedSound) }) {
                        Text(
                            text = "Save",
                            color = Color.Yellow,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
        ) {
            availableSounds.forEach { sound ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(
                            if (selectedSound == sound) Color(0xFF9D9D9D) else Color.Transparent
                        )
                        .clickable {
                            selectedSound = sound
                            onSoundSelected(sound)

                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            mediaPlayer = null

                            val soundResId = getSoundResourceId(sound)
                            if (soundResId != null) {
                                mediaPlayer = MediaPlayer.create(context, soundResId)
                                mediaPlayer?.start()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween // Thêm SpaceBetween để điều chỉnh
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedSound == sound),
                            onClick = { selectedSound = sound }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sound,
                            fontSize = 16.sp,
                            color = if (selectedSound == sound) Color.White else Color.Gray
                        )
                    }

                    if (selectedSound == sound) {
                        WaveformAnimation(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 16.dp) // Khoảng cách với lề phải
                        )
                    }
                }

            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}

fun getSoundResourceId(soundName: String): Int? {
    return when (soundName) {
        "Morning Chime" -> R.raw.morning_chime
        "Nature Melody" -> R.raw.nature_melody
        else -> null
    }
}


@Composable
fun WaveformAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val scale3 by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing, delayMillis = 400),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(
        modifier = modifier.height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp, 24.dp)
                .scale(scale1)
                .background(Color.Green, shape = RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .size(6.dp, 24.dp)
                .scale(scale2)
                .background(Color.Green, shape = RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .size(6.dp, 24.dp)
                .scale(scale3)
                .background(Color.Green, shape = RoundedCornerShape(2.dp))
        )
    }
}