package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmCard(
    label: String,
    time: String,
    days: List<String>,  // e.g., ["", "", "", "W", "T", "F", "S"]
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var alarmEnabled by remember { mutableStateOf(isEnabled) }
    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")

    // Identify selected days based on non-empty values in the 'days' list
    val selectedDays = days.mapIndexedNotNull { index, day ->
        if (day.isNotBlank()) daysOfWeek[index] else null
    }

    // Check if all days are selected
    val allDaysSelected = selectedDays.size == 7 && selectedDays.containsAll(daysOfWeek)
    val displayDays = if (allDaysSelected) "Everyday" else null

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(180.dp),
        colors = CardDefaults.cardColors(
            containerColor = CustomColors.background,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1st row: Alarm Label
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CustomColors.fontColor
            )

            // 2nd row: Time with AM/PM
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time,
                    fontSize = 16.sp,  // Larger font size for time
                    fontWeight = FontWeight.Bold,
                    color = if (alarmEnabled) CustomColors.onPrimary else CustomColors.fontColor,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "AM",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (alarmEnabled) CustomColors.onPrimary else CustomColors.fontColor,
                )
            }

            // 3rd row: Days or Everyday
            if (displayDays != null) {
                Text(
                    text = displayDays,
                    fontSize = 10.sp,
                    color = if (alarmEnabled) CustomColors.onPrimary else CustomColors.onSecondary,
                    textAlign = TextAlign.Start,
                )
            } else {
                // Show individual days with color indication for selection
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    daysOfWeek.forEachIndexed { index, day ->
                        val isSelected = days[index].isNotBlank()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = day,
                                fontSize = 12.sp,
                                color = if (isSelected) CustomColors.onPrimary else CustomColors.onSecondary,
                            )
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(
                                        color = if (isSelected) CustomColors.onPrimary else CustomColors.onSecondary,
                                        shape = RoundedCornerShape(50)
                                    )
                            )
                        }
                    }
                }
            }

            // Last row: Switch aligned to the right
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Switch(
                    checked = alarmEnabled,
                    onCheckedChange = {
                        alarmEnabled = it
                        onToggle(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CustomColors.onPrimary,
                        uncheckedThumbColor = CustomColors.onSecondary,
                    )
                )
            }
        }
    }
}