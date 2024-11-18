package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.R.attr.onClick
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
    time: Time = Time(0, 0, amPm = ""),
    days: List<String>,  // e.g., ["", "", "", "W", "T", "F", "S"]
    isEnabled: Boolean,
    onClick: () -> Unit,
    onDelete:() -> Unit,
    onSwitchChange: (Boolean) -> Unit
) {
    var alarmEnabled by remember { mutableStateOf(isEnabled) }
    val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")

    val displayDays =
        when {
           days[1]=="M" && days[2]=="T"&&  days[3]=="W" &&  days[4]=="T" &&  days[5]=="F" &&  days[0]=="" && days[6]==""-> "Weekday"
            days[1]=="" &&  days[2]==""&&  days[3]=="" &&  days[4]=="" &&  days[5]=="" &&  days[0]=="" && days[6]=="" -> "Never"
            days[1]=="" &&  days[2]==""&&  days[3]=="" &&  days[4]=="" &&  days[5]=="" &&  days[0]=="S" && days[6]=="S"-> "Weekend"
            days[1]=="M" &&  days[2]=="T"&&  days[3]=="W" &&  days[4]=="T" &&  days[5]=="F" &&  days[0]=="S" && days[6]=="S" -> "Everyday"
            else -> {
                // Convert array indices to day names
                val dayNames = mutableListOf<String>()
                days.forEachIndexed { index, value ->
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

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(180.dp)
            .clickable { onClick() },
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
                    text = "${time.hour} : ${time.minute.toString().padStart(2, '0')} ",
                    fontSize = 16.sp,  // Larger font size for time
                    fontWeight = FontWeight.Bold,
                    color = if (alarmEnabled) CustomColors.onPrimary else CustomColors.fontColor,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = time.amPm,
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
                        val isSelected = days[index].isNotEmpty()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text =  day ,
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
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
                    }
                    Switch(
                        checked = alarmEnabled,
                        onCheckedChange = {
                                isChecked ->
                            alarmEnabled = isChecked
                            onSwitchChange(isChecked)

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
}