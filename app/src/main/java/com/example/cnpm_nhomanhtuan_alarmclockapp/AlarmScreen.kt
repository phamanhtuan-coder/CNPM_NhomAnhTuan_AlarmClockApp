package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    navController: NavHostController
) {
    return Scaffold(
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
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.SettingScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Settings",
                            tint = CustomColors.fontColor

                        )
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
                        IconButton(onClick = {
                            navController.navigate(Screen.AlarmScreen.route)
                        }) {
                            Icon(Icons.Filled.Alarm, contentDescription = "Alarm", tint = CustomColors.onPrimary)
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.AlarmScreen.route)
                        }) {
                            Icon(Icons.Filled.AlarmOn, contentDescription = "Clock", tint = CustomColors.fontColor)
                        }
                    }
                    // Right Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            navController.navigate(Screen.TimerScreen.route)
                        }) {
                            Icon(Icons.Filled.Timer, contentDescription = "Timer", tint = CustomColors.fontColor)
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.StopwatchScreen.route)
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
                    navController.navigate(Screen.AlarmDetailScreen.route)
                },
                content={Icon(Icons.Filled.Add, contentDescription = "Add Alarm")}
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomColors.surface)
                    .padding(8.dp).padding(innerPadding),
            ) {
                items(10) { index ->
                    AlarmCard(
                        label = "Alarm $index",
                        time = "10:00 AM",
                        days = listOf("","","","W", "T", "F", "S"),
                        isEnabled = index % 2 == 0,
                        onToggle = { }
                    )
                }
            }
        },
    )
}