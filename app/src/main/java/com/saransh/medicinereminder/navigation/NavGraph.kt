package com.saransh.medicinereminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.saransh.medicinereminder.screens.AddScheduleBasicInfoScreen
import com.saransh.medicinereminder.screens.AddScheduleTimeScreen
import com.saransh.medicinereminder.screens.TodayScheduleScreen
import com.saransh.medicinereminder.viewmodel.TodayScheduleViewModel
import com.saransh.medicinereminder.models.DailySchedule
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NavGraph(
    navController: NavHostController,
    todayScheduleViewModel: TodayScheduleViewModel
) {
    NavHost(navController = navController, startDestination = "todaySchedule") {

        // TODAY SCHEDULE SCREEN
        composable("todaySchedule") {
            val todaySchedules by todayScheduleViewModel.todaySchedules.collectAsState()
            TodayScheduleScreen(
                todaySchedules = todaySchedules,
                todayScheduleViewModel = todayScheduleViewModel, // ✅ Fixed
                onAddClick = { navController.navigate("addScheduleBasicInfo") }
            )
        }

        // ADD BASIC INFO SCREEN
        composable("addScheduleBasicInfo") {
            AddScheduleBasicInfoScreen(
                onNext = { medicineName, doctorName, frequencyCount, frequencyType ->
                    navController.navigate(
                        "addScheduleTime/$medicineName/$doctorName/$frequencyCount/$frequencyType"
                    )
                }
            )
        }

        // ADD TIME SCREEN
        composable(
            route = "addScheduleTime/{medicineName}/{doctorName}/{frequencyCount}/{frequencyType}",
            arguments = listOf(
                navArgument("medicineName") { type = NavType.StringType },
                navArgument("doctorName") { type = NavType.StringType; defaultValue = "" },
                navArgument("frequencyCount") { type = NavType.IntType },
                navArgument("frequencyType") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val medicineName = backStackEntry.arguments?.getString("medicineName") ?: ""
            val doctorName = backStackEntry.arguments?.getString("doctorName")?.takeIf { it.isNotEmpty() }
            val frequencyCount = backStackEntry.arguments?.getInt("frequencyCount") ?: 1
            val frequencyType = backStackEntry.arguments?.getString("frequencyType") ?: "Daily"

            AddScheduleTimeScreen(
                medicineName = medicineName,
                doctorName = doctorName,
                frequencyCount = frequencyCount,
                frequencyType = frequencyType,
                onSave = { times, _repeats ->
                    val today = Date()
                    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(today)

                    times.forEach { time ->
                        if (time.isNotEmpty()) {
                            val schedule = DailySchedule(
                                date = today.time,
                                dayOfWeek = dayOfWeek,
                                startTime = time,
                                endTime = "",
                                taskName = medicineName
                            )
                            todayScheduleViewModel.addSchedule(schedule)
                        }
                    }
                    navController.popBackStack("todaySchedule", inclusive = false)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
