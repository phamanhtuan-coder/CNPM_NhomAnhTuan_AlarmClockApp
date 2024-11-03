package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmDetailsScreen(
    alarm: Alarm?,
    onSave: (Alarm) -> Unit,
    onCancel: () -> Unit
) {
    var label by remember { mutableStateOf(alarm?.label ?: "") }
    var time by remember { mutableStateOf(alarm?.time ?: "10:00 AM") }
    var days by remember { mutableStateOf(alarm?.days ?: listOf("", "", "", "", "", "", "")) }
    var isEnabled by remember { mutableStateOf(alarm?.isEnabled ?: false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Alarm Label Field
        Text(text = "Label", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        BasicTextField(
            value = label,
            onValueChange = { label = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time Field (Placeholder)
        Text(text = "Time", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        BasicTextField(
            value = time,
            onValueChange = { time = it },
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
            val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
            daysOfWeek.forEachIndexed { index, day ->
                TextButton(
                    onClick = {
                        days = days.mapIndexed { i, d -> if (i == index) day else d }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (days[index].isNotBlank()) CustomColors.onPrimary else CustomColors.onSecondary
                    )
                ) {
                    Text(text = day)
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
                checked = isEnabled,
                onCheckedChange = { isEnabled = it }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save and Cancel Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val newAlarm = Alarm(
                        id = alarm?.id ?: 0,
                        label = label,
                        time = time,
                        days = days,
                        isEnabled = isEnabled
                    )
                    onSave(newAlarm)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save")
            }
        }
    }
}