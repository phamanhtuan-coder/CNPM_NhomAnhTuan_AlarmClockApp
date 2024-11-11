package com.example.cnpm_nhomanhtuan_alarmclockapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

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
        composable(Screen.AlarmDetailScreen.route+ "?id={id}",
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
    }
}