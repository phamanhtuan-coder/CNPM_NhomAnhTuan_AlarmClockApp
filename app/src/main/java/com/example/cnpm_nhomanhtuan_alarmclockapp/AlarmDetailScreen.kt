package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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
        bottomBar = {
            BottomActionBar(
                onCancelClick = { navController.popBackStack() },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
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

                    Spacer(modifier = Modifier.height(24.dp))

                    AlarmSettingsCard()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmSettingsCard() {
    var selectedDays by remember { mutableStateOf(mutableSetOf<Int>()) }
    var alarmName by remember { mutableStateOf(TextFieldValue("")) }

    val selectedDaysText = remember(selectedDays) {
        when {
            selectedDays.containsAll(listOf(1, 2, 3, 4, 5)) && selectedDays.size == 5 -> "Weekdays"
            selectedDays.containsAll(listOf(0, 6)) && selectedDays.size == 2 -> "Weekend"
            selectedDays.size == 7 -> "Everyday"
            else -> selectedDays.joinToString(", ") { dayIndex ->
                when (dayIndex) {
                    0 -> "Sun"
                    1 -> "Mon"
                    2 -> "Tue"
                    3 -> "Wed"
                    4 -> "Thu"
                    5 -> "Fri"
                    6 -> "Sat"
                    else -> ""
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .fillMaxHeight(1f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDaysText,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEachIndexed { index, day ->
                    TextButton(
                        onClick = {
                            selectedDays = selectedDays.toMutableSet().apply {
                                if (contains(index)) remove(index) else add(index)
                            }
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = day,
                            color = if (selectedDays.contains(index)) Color.Red else Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = alarmName,
                onValueChange = { alarmName = it },
                label = { Text(
                    "Alarm name",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                        .align(Alignment.Start)
                )},
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                singleLine = true,
                modifier = Modifier
                    .fillMaxSize(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                    containerColor = Color.Transparent
                )
            )
            AlarmSettingItem(title = "Alarm sound", value = "Alarm Army")
            AlarmSettingItem(title = "Vibration", value = "Basic call")
            AlarmSettingItem(title = "Snooze", value = "5 minutes, Forever")
            AlarmSettingItem(title = "Alarm background", value = "")
        }
    }
}

@Composable
fun AlarmSettingItem(title: String, value: String) {
    var isChecked by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            if (value.isNotEmpty()) {
                Text(text = value, color = Color.Gray, fontSize = 14.sp)
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFBB86FC),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}

@Composable
fun BottomActionBar(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onCancelClick) {
            Text(
                text = "Cancel",
                color = Color(0xFFBB86FC),
                fontSize = 16.sp
            )
        }

        TextButton(onClick = onSaveClick) {
            Text(
                text = "Save",
                color = Color(0xFFBB86FC),
                fontSize = 16.sp
            )
        }
    }
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

    Box(
        modifier = modifier.height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Text(
                text = ":",
                fontSize = 36.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

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

            LazyColumn(
                state = amPmState,
                modifier = Modifier
                    .height(120.dp)
                    .width(80.dp)
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(amPmList.size * 100) { i ->
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

    DisposableEffect(hourState.firstVisibleItemIndex, minuteState.firstVisibleItemIndex, amPmState.firstVisibleItemIndex) {
        val selectedHour = hoursList[if (hourState.firstVisibleItemIndex % hoursList.size == 11) 0 else hourState.firstVisibleItemIndex % hoursList.size + 1]
        val selectedMinute = minutesList[if (minuteState.firstVisibleItemIndex % minutesList.size == 59) 0 else minuteState.firstVisibleItemIndex % minutesList.size + 1]
        val selectedAmPm = amPmList[if (amPmState.firstVisibleItemIndex % amPmList.size == 1) 0 else 1]
        onTimeSelected(Time(selectedHour, selectedMinute, selectedAmPm))
        onDispose { }
    }
}