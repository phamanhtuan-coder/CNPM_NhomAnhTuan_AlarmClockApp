package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

data class Time(val hour: Int, val minute: Int, val amPm: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailsScreen(
    navController: NavHostController,
    id: Int = -1
) {
    var selectedTime by remember { mutableStateOf(Time(6, 0, "AM")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center // Căn giữa toàn bộ nội dung
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    EndlessRollingPadlockTimePicker(
                        modifier = Modifier.fillMaxWidth(),
                        onTimeSelected = { time ->
                            selectedTime = time
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Selected Time: ${selectedTime.hour}:${(selectedTime.minute).toString().padStart(2, '0')} ${selectedTime.amPm}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}

@Composable
fun EndlessRollingPadlockTimePicker(
    modifier: Modifier = Modifier,
    onTimeSelected: (Time) -> Unit
) {
    val hoursList = (1..12).toList()
    val minutesList = (0..59).toList()
    val amPmList = listOf("AM", "PM")

    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = hoursList.indexOf(5) + hoursList.size * 50)
    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = minutesList.indexOf(59) + minutesList.size * 50)
    val amPmState = rememberLazyListState(initialFirstVisibleItemIndex = amPmList.indexOf("PM") + amPmList.size * 25)

    val scope = rememberCoroutineScope()

    // Thêm Box để căn giữa nội dung Row
    Box(
        modifier = modifier.height(120.dp), // Đảm bảo chiều cao đồng đều giữa các cột
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LazyColumn cho giờ
            LazyColumn(
                state = hourState,
                modifier = Modifier
                    .height(120.dp)
                    .width(60.dp)
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(hoursList.size * 100) { i ->
                    val hour = hoursList[i % hoursList.size]
                    val isSelected = (i == hourState.firstVisibleItemIndex + 1)
                    Text(
                        text = if (hour == 0) "12" else hour.toString(),
                        fontSize = if (isSelected) 36.sp else 28.sp,
                        color = Color.White.copy(alpha = if (isSelected) 1f else 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (isSelected) 4.dp else 2.dp)
                    )
                }
            }

            // Dấu ":"
            Text(
                text = ":",
                fontSize = 36.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // LazyColumn cho phút
            LazyColumn(
                state = minuteState,
                modifier = Modifier
                    .height(120.dp)
                    .width(60.dp)
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(minutesList.size * 100) { i ->
                    val minute = minutesList[i % minutesList.size]
                    val isSelected = (i == minuteState.firstVisibleItemIndex + 1)
                    Text(
                        text = minute.toString().padStart(2, '0'),
                        fontSize = if (isSelected) 36.sp else 28.sp,
                        color = Color.White.copy(alpha = if (isSelected) 1f else 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (isSelected) 4.dp else 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // LazyColumn cho AM/PM
            LazyColumn(
                state = amPmState,
                modifier = Modifier
                    .height(120.dp)
                    .width(80.dp)
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(amPmList.size * 100) { i -> // Lặp lại AM/PM để cuộn
                    val amPm = amPmList[i % amPmList.size]
                    val isSelected = (i == amPmState.firstVisibleItemIndex + 1)
                    Text(
                        text = amPm,
                        fontSize = if (isSelected) 36.sp else 28.sp,
                        color = Color.White.copy(alpha = if (isSelected) 1f else 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (isSelected) 4.dp else 2.dp)
                    )
                }
            }
        }
    }

    // Điều chỉnh vị trí cuộn sau khi cuộn dừng lại để giữ mục được chọn luôn ở giữa
    LaunchedEffect(hourState.isScrollInProgress) {
        if (!hourState.isScrollInProgress) {
            scope.launch {
                val index = hourState.firstVisibleItemIndex % hoursList.size
                hourState.scrollToItem(index + hoursList.size * 50)
            }
        }
    }

    LaunchedEffect(minuteState.isScrollInProgress) {
        if (!minuteState.isScrollInProgress) {
            scope.launch {
                val index = minuteState.firstVisibleItemIndex % minutesList.size
                minuteState.scrollToItem(index + minutesList.size * 50)
            }
        }
    }

    LaunchedEffect(amPmState.isScrollInProgress) {
        if (!amPmState.isScrollInProgress) {
            scope.launch {
                val index = amPmState.firstVisibleItemIndex % amPmList.size
                amPmState.scrollToItem(index + amPmList.size * 50)
            }
        }
    }

    // Cập nhật thời gian sau khi cuộn dừng
    DisposableEffect(hourState.firstVisibleItemIndex, minuteState.firstVisibleItemIndex, amPmState.firstVisibleItemIndex) {
        val selectedHour = hoursList[if (hourState.firstVisibleItemIndex % hoursList.size == 11) 0 else hourState.firstVisibleItemIndex % hoursList.size + 1]
        val selectedMinute = minutesList[if (minuteState.firstVisibleItemIndex % minutesList.size == 59) 0 else minuteState.firstVisibleItemIndex % minutesList.size + 1]
        val selectedAmPm = amPmList[if (amPmState.firstVisibleItemIndex % amPmList.size == 1) 0 else 1]
        onTimeSelected(Time(selectedHour, selectedMinute, selectedAmPm))
        onDispose { }
    }
}