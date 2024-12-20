package com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.viewmodel.AlarmScreenViewModel
import com.example.cnpm_nhomanhtuan_alarmclockapp.utils.CustomColors
import com.example.cnpm_nhomanhtuan_alarmclockapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    navController: NavHostController,
) {
    val viewModel: AlarmScreenViewModel = viewModel(
        modelClass = AlarmScreenViewModel::class.java
    )
    val alarmScreenState = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Alarm",
                        color = CustomColors.fontColor,
                        textAlign = TextAlign.Left
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomColors.background,
                ),
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Settings", tint = CustomColors.fontColor)
                    }
                }
            )
        },
        contentColor = CustomColors.primary,
        bottomBar = {
            BottomAppBar(
                containerColor = CustomColors.secondary,
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { navController.navigate(Screen.AlarmScreen.route) }) {
                            Icon(Icons.Filled.Alarm, contentDescription = "Alarm", tint = CustomColors.onPrimary)
                        }
                        IconButton(onClick = { navController.navigate(Screen.AlarmScreen.route) }) {
                            Icon(Icons.Filled.AlarmOn, contentDescription = "Clock", tint = CustomColors.fontColor)
                        }
                    }
                    // Right Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Filled.Timer, contentDescription = "Timer", tint = CustomColors.fontColor)
                        }
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Filled.AvTimer, contentDescription = "Stopwatch", tint = CustomColors.fontColor)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.offset(y = 64.dp),
                containerColor = CustomColors.buttonPrimary,
                contentColor = CustomColors.secondary,
                shape = CircleShape,
                onClick = {
                    navController.navigate("${Screen.AlarmDetailScreen.route}?id=-1")
                },
                content = { Icon(Icons.Filled.Add, contentDescription = "Add Alarm") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomColors.surface)
                    .padding(8.dp)
                    .padding(innerPadding),
            ) {
                items (alarmScreenState.alarms){alarm ->
//                    Text("${alarm.id}")
                    AlarmCard(
                        label = alarm.label.ifEmpty { "Alarm" },
                        time = alarm.time,
                        days =  alarm.days,
                        isEnabled = alarm.isEnabled,

                        onClick = {
                            navController.navigate("${Screen.AlarmDetailScreen.route}?id=${alarm.id}")
                        },
                        onDelete = {
                            viewModel.deleteAlarm(alarm)
                        },
                       onSwitchChange = {
                           val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
                           viewModel.updateAlarm(updatedAlarm)
                       }
                    )
                }
            }
        },

    )
}