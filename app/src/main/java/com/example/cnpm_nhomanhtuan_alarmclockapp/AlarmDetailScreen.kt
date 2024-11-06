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
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailsScreen(
    navController: NavHostController,
    id: Int = -1
) {


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
                    value = "",
                    onValueChange = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color.LightGray)
                )




                Spacer(modifier = Modifier.height(16.dp))

                // Time Field (Placeholder)
                Text(text = "Time", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                BasicTextField(
                    value ="", // Ensuring time is a String
                    onValueChange = {

                    },
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
                        checked =true,
                        onCheckedChange = {

                        }
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