package com.example.cnpm_nhomanhtuan_alarmclockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.theme.CNPM_NhomAnhTuan_AlarmClockAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
            setContent {
                CNPM_NhomAnhTuan_AlarmClockAppTheme {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController
                    )
            }
        }
    }
}