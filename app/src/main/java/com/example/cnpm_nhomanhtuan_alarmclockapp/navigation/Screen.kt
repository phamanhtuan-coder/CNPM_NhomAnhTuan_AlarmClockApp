package com.example.cnpm_nhomanhtuan_alarmclockapp

sealed class Screen(val route: String) {
    object AlarmScreen : Screen("AlarmScreen")
    object AlarmDetailScreen : Screen("AlarmDetailScreen")
//    object TimerScreen : Screen("TimerScreen")
//    object StopwatchScreen : Screen("StopWatchScreen")
//    object SettingScreen : Screen("SettingScreen")
}