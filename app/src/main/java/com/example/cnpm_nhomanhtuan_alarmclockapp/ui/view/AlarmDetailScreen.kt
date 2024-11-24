package com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Alarm
import com.example.cnpm_nhomanhtuan_alarmclockapp.utils.AlarmScheduler
import com.example.cnpm_nhomanhtuan_alarmclockapp.utils.CustomColors
import com.example.cnpm_nhomanhtuan_alarmclockapp.data.model.Time
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.viewmodel.AlarmDetailViewModel
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.viewmodel.AlarmDetailViewModelFactor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailsScreen(
   navController: NavHostController,
    id: Int = -1,
) {
    val context = LocalContext.current
    val viewModel = viewModel<AlarmDetailViewModel>(
        factory = AlarmDetailViewModelFactor(id)
    )
    val alarmState = viewModel.state

    var selectedSound by remember { mutableStateOf("") }
    var selectedTime by remember{ mutableStateOf(alarmState.time) }
    var soundResourcePath by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(alarmState.days.toTypedArray().ifEmpty { Array(7) { "" } }) }
    var alarmName by remember{ mutableStateOf(TextFieldValue(alarmState.label)) }



    LaunchedEffect(selectedSound) {
        val resourceId = getSoundResourceId(selectedSound)
        soundResourcePath =
            if (resourceId != null) "res/raw/${context.resources.getResourceEntryName(resourceId)}" else "Unknown"
    }

    LaunchedEffect(alarmState.time) {
        selectedTime = alarmState.time
    }

    LaunchedEffect(alarmState.days) {
        selectedDays = alarmState.days.toTypedArray().ifEmpty { Array(7) { "" } }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(navController.currentBackStackEntry) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("selectedSound")
            ?.observe(lifecycleOwner) { newSound ->
                selectedSound = newSound
            }
    }


    LaunchedEffect(alarmState.label) {
        alarmName = TextFieldValue(alarmState.label)
    }
    LaunchedEffect(alarmState) {
        if (alarmState.sound.isNotEmpty()) {
            selectedSound = when (alarmState.sound) {
                "res/raw/morning_chime" -> "Morning Chime"
                "res/raw/nature_melody" -> "Nature Melody"
                else -> "Morning Chime"
            }
            soundResourcePath = alarmState.sound
        } else {
            soundResourcePath = "res/raw/morning_chime"
        }

        selectedTime = alarmState.time
        selectedDays = alarmState.days.toTypedArray().ifEmpty { Array(7) { "" } }
        alarmName = TextFieldValue(alarmState.label)
    }

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
            @Suppress("NAME_SHADOWING") val context = LocalContext.current
            BottomActionBar(
                onCancelClick = {
                    navController.popBackStack()
                                },
                onSaveClick = {
                    val alarm = Alarm(
                        id = if (id > 0) id else 0,
                        label = alarmName.text,
                        time = selectedTime,
                        days = selectedDays.toList(),
                        sound = soundResourcePath,
                        isEnabled = true
                    )
                    if (alarm.id > 0) { // Update existing alarm
                        viewModel.updateAlarm(alarm, context)
                    } else { // Create new alarm
                        viewModel.insertAlarm(alarm)
                    }
                    Log.d("AlarmDetail", "Editing Alarm with ID: $id")
                    Log.d("AlarmState", "Current State ID: ${alarmState.id}")

                    // Lên lịch báo thức
                    AlarmScheduler.scheduleAlarmIfEnabled(
                        context = navController.context,
                        alarm = alarm

                    )

                    navController.popBackStack()
                }

            )
        },
        contentColor = CustomColors.primary,
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
                    Text(text=id.toString(), color = Color.White)
                    EndlessRollingPadlockTimePicker(
                        modifier = Modifier.fillMaxWidth(),
                        onTimeSelected = { time -> selectedTime = time },
                        initialTime = selectedTime,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Selected Time: ${selectedTime.hour}:${(selectedTime.minute).toString().padStart(2, '0')} ${selectedTime.amPm}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    @Suppress("NAME_SHADOWING") val lifecycleOwner = LocalLifecycleOwner.current

                    LaunchedEffect(navController.currentBackStackEntry) {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.getLiveData<String>("selectedSound")
                            ?.observe(lifecycleOwner) { newSound ->
                                selectedSound = newSound
                            }
                    }


                    AlarmSettingsCard(
                        selectedDays = selectedDays,
                        alarmName = alarmName,
                        selectedSound = selectedSound,
                        onDaysChange = { updatedDays -> selectedDays = updatedDays },
                        onNameChange = { updatedName -> alarmName = updatedName },
                        onSoundChange = {

                            navController.navigate("sound_picker/$selectedSound")
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmSettingsCard(
    selectedDays: Array<String> ,
    alarmName: TextFieldValue,
    selectedSound: String,
    onDaysChange: (Array<String>) -> Unit,
    onNameChange: (TextFieldValue) -> Unit,
    onSoundChange: () -> Unit
) {
    val selectedDaysText = remember(selectedDays) {
        when {
            selectedDays[1]=="M" && selectedDays[2]=="T"&& selectedDays[3]=="W" && selectedDays[4]=="T" && selectedDays[5]=="F" &&  selectedDays[0]=="" && selectedDays[6]==""-> "Weekday"
            selectedDays[1]=="" && selectedDays[2]==""&& selectedDays[3]=="" && selectedDays[4]=="" && selectedDays[5]=="" &&  selectedDays[0]=="" && selectedDays[6]=="" -> "Never"
            selectedDays[1]=="" && selectedDays[2]==""&& selectedDays[3]=="" && selectedDays[4]=="" && selectedDays[5]=="" &&  selectedDays[0]=="S" && selectedDays[6]=="S"-> "Weekend"
            selectedDays[1]=="M" && selectedDays[2]=="T"&& selectedDays[3]=="W" && selectedDays[4]=="T" && selectedDays[5]=="F" &&  selectedDays[0]=="S" && selectedDays[6]=="S" -> "Everyday"
            else -> {
                // Convert array indices to day names
                val dayNames = mutableListOf<String>()
                selectedDays.forEachIndexed { index, value ->
                    if (value.isNotEmpty()) {
                        dayNames.add(
                            when (index) {
                                0 -> "Sun"
                                1 -> "Mon"
                                2 -> "Tue"
                                3 -> "Wed"
                                4 -> "Thu"
                                5 -> "Fri"
                                6 -> "Sat"
                                else -> ""
                            }
                        )
                    }
                }
                dayNames.joinToString(", ")
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .fillMaxHeight(1f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = CustomColors.secondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
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
                    .padding(vertical = 8.dp)
                   ,
                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
                val daysArray = arrayOf("S", "M", "T", "W", "T", "F", "S")
                daysArray.forEachIndexed { index, day ->
                    TextButton(
                        modifier = Modifier.size(50.dp,50.dp),
                        onClick = {
                            val updatedDays = selectedDays.copyOf()
                            updatedDays[index] = if (updatedDays[index].isEmpty()) day else ""
                            onDaysChange(updatedDays)
                        },
                        contentPadding = PaddingValues(0.dp)

                    ) {
                        Text(
                            text = day,
                            color = if (selectedDays[index].isNotEmpty()) Color.Red else Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = alarmName,
                onValueChange = onNameChange,
                label = { Text("Alarm name", color = Color.Gray) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                singleLine = true,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-14).dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height (8.dp))
            AlarmSettingItem(
                title = "Alarm sound",
                value = selectedSound,
                onClick = onSoundChange
            )
            AlarmSettingItem(title = "Vibration", value = "Basic call")
            AlarmSettingItem(title = "Snooze", value = "5 minutes, Forever")

        }
    }
}

@Composable
fun AlarmSettingItem(title: String, value: String, onClick: () -> Unit = {}) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = CustomColors.secondary)
    ){
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

//            Switch(
//                checked = isChecked,
//                onCheckedChange = { isChecked = it },
//                colors = SwitchDefaults.colors(
//                    checkedThumbColor = CustomColors.onPrimary,
//                    uncheckedThumbColor = CustomColors.onSecondary,
//                )
//            )
        }
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
                //color = Color(0xFFBB86FC),
                color = CustomColors.buttonPrimary,
                fontSize = 16.sp
            )
        }

        TextButton(onClick = onSaveClick) {
            Text(
                text = "Save",
                //color = Color(0xFFBB86FC),
                color = CustomColors.buttonPrimary,
                fontSize = 16.sp
            )
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun EndlessRollingPadlockTimePicker(
    modifier: Modifier = Modifier,
    initialTime: Time,
    onTimeSelected: (Time) -> Unit
) {
//    val hoursList = (1..12).toList()
//    val minutesList = (0..59).toList()
//    val amPmList = listOf("AM", "PM")
//
//
//    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = hoursList.indexOf(initialTime.hour-1) + hoursList.size * 50)
//    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = minutesList.indexOf(initialTime.minute-1) + minutesList.size * 50)
//    val amPmState = rememberLazyListState(initialFirstVisibleItemIndex = if(amPmList.indexOf(initialTime.amPm) == 1) 0 else 1 + amPmList.size * 50)

    val hoursList = (1..12).toList()
    val minutesList = (0..59).toList()
    val amPmList = listOf("AM", "PM")

    val hourState = rememberLazyListState()
    val minuteState = rememberLazyListState()
    val amPmState = rememberLazyListState()

    LaunchedEffect(initialTime) {
        hourState.scrollToItem(hoursList.indexOf(initialTime.hour - 1) + hoursList.size * 50)
        minuteState.scrollToItem(minutesList.indexOf(initialTime.minute - 1) + minutesList.size * 50)
        amPmState.scrollToItem(if (initialTime.amPm == "AM") 1 else 0 + amPmList.size * 50)
    }


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

    LaunchedEffect(hourState.isScrollInProgress, minuteState.isScrollInProgress, amPmState.isScrollInProgress) {
        if (!hourState.isScrollInProgress && !minuteState.isScrollInProgress && !amPmState.isScrollInProgress) {
            // Tính chỉ số dựa vào vị trí hiện tại, thêm +1 để lấy mục kế tiếp chính xác
            val selectedHour = hoursList[(hourState.firstVisibleItemIndex + 1) % hoursList.size]
            val selectedMinute = minutesList[(minuteState.firstVisibleItemIndex + 1) % minutesList.size]
            val selectedAmPm = amPmList[(amPmState.firstVisibleItemIndex + 1) % amPmList.size]

            onTimeSelected(Time(selectedHour, selectedMinute, selectedAmPm))
        }
    }




}