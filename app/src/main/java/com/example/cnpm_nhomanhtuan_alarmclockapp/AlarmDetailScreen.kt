package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailsScreen(
    navController: NavHostController,
    id: Int = -1
) {
    val viewModel: AlarmDetailViewModel = viewModel(factory = AlarmDetailViewModelFactory(id))
    val alarmState = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (id < 0) "Add New Alarm" else "Edit Alarm") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Alarm Label Field
                Text(text = "Label", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                BasicTextField(
                    value = alarmState.label.toString(), // Ensuring label is a String
                    onValueChange = { viewModel.onChangeLabel(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.LightGray)
                )




                Spacer(modifier = Modifier.height(16.dp))

                // Time Field (Placeholder)
                Text(text = "Time", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                BasicTextField(
                    value = alarmState.time.toString(), // Ensuring time is a String
                    onValueChange = { viewModel.onChangeTime(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Days Selection
                Text(text = "Days", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val daysOfWeek = DayOfWeek.values().toList()
                    daysOfWeek.forEachIndexed { index, dayOfWeek ->
                        TextButton(
                            onClick = { viewModel.onToggleDay(dayOfWeek) }, // Pass the DayOfWeek enum
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (alarmState.selectedDays.contains(dayOfWeek))
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray
                            )
                        ) {
                            Text(text = dayOfWeek.shortName) // Display the short name
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Enable/Disable Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Enabled", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = alarmState.isEnabled,
                        onCheckedChange = { viewModel.onToggleEnabled(it) }
                    )

                }

                Spacer(modifier = Modifier.weight(1f))

                // Save and Cancel Buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            viewModel.saveAlarm()
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    )
}