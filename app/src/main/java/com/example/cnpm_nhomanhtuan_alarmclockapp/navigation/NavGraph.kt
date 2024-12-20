package com.example.cnpm_nhomanhtuan_alarmclockapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view.AlarmDetailsScreen
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view.AlarmScreen
import com.example.cnpm_nhomanhtuan_alarmclockapp.ui.view.SoundPickerScreen

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.AlarmScreen.route,
    ) {
        composable(Screen.AlarmScreen.route) {
            AlarmScreen(navController = navController)
        }
        composable(
            Screen.AlarmDetailScreen.route+ "?id={id}",
            arguments = listOf(navArgument("id") { nullable=true })
            ) {
            val id = it.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                AlarmDetailsScreen(
                    navController = navController,
                    id = id)
            } else {
                navController.navigate(Screen.AlarmScreen.route)
            }
        }
        composable(
            route = "sound_picker/{currentSound}",
            arguments = listOf(navArgument("currentSound") { type = NavType.StringType })
        ) { backStackEntry ->
            val currentSound = backStackEntry.arguments?.getString("currentSound") ?: "Alarm Army"

            SoundPickerScreen(
                currentSound = currentSound,

                onSoundSelected = {
                    // Logic xử lý khi chọn âm thanh (nếu cần)
                },
                onSave = { selectedSound ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedSound", selectedSound) // Cập nhật âm thanh đã chọn
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack() // Hủy và quay lại màn hình trước
                }
            )
        }



    }
}