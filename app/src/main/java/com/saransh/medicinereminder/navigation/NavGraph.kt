package com.saransh.medicinereminder.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.saransh.medicinereminder.data.MedicineRepository
import com.saransh.medicinereminder.screens.AddScheduleBasicInfoScreen
import com.saransh.medicinereminder.screens.AddScheduleTimeScreen
import com.saransh.medicinereminder.screens.TodayScheduleScreen
import com.saransh.medicinereminder.utils.getTodayMidnightEpoch
import com.saransh.medicinereminder.viewmodel.TodayScheduleViewModel
import com.saransh.medicinereminder.viewmodel.TodayScheduleViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: MedicineRepository,
    modifier: Modifier = Modifier
) {
    val todayScheduleViewModel: TodayScheduleViewModel = viewModel(
        factory = TodayScheduleViewModelFactory(repository)
    )

    val todaySchedulesState = todayScheduleViewModel.todaySchedules.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "today_schedule",
        modifier = modifier
    ) {
        // TODAY
        composable("today_schedule") {
            TodayScheduleScreen(
                todaySchedules = todaySchedulesState.value,
                todayScheduleViewModel = todayScheduleViewModel,
                onAddClick = { navController.navigate("add_schedule_basic_info") }
            )
        }

        // BASIC INFO
        composable("add_schedule_basic_info") {
            AddScheduleBasicInfoScreen { medicineName, doctorName, freqCount, freqType ->
                val m = Uri.encode(medicineName)
                val d = Uri.encode(doctorName ?: "")
                val t = Uri.encode(freqType)
                navController.navigate("add_schedule_time/$m/$d/$freqCount/$t")
            }
        }

        // TIME SELECTION
        composable(
            route = "add_schedule_time/{medicineName}/{doctorName}/{freqCount}/{freqType}",
            arguments = listOf(
                navArgument("medicineName") { type = NavType.StringType },
                navArgument("doctorName") { type = NavType.StringType; defaultValue = "" },
                navArgument("freqCount") { type = NavType.IntType },
                navArgument("freqType") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val medicineNameArg = Uri.decode(backStackEntry.arguments?.getString("medicineName") ?: "")
            val doctorNameArg = Uri.decode(backStackEntry.arguments?.getString("doctorName") ?: "")
            val freqCountArg = backStackEntry.arguments?.getInt("freqCount") ?: 1
            val freqTypeArg = Uri.decode(backStackEntry.arguments?.getString("freqType") ?: "")

            AddScheduleTimeScreen(
                medicineName = medicineNameArg,
                doctorName = doctorNameArg,
                frequencyCount = freqCountArg,
                frequencyType = freqTypeArg,
                onSave = { medicineName, times ->
                    val todayMidnight = getTodayMidnightEpoch()
                    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())

                    times.forEach { time ->
                        if (time.isNotEmpty()) {
                            val schedule = com.saransh.medicinereminder.models.DailySchedule(
                                date = todayMidnight,
                                dayOfWeek = dayOfWeek,
                                startTime = time,
                                endTime = "",
                                taskName = medicineName
                            )
                            todayScheduleViewModel.addSchedule(schedule)
                        }
                    }
                    // ✅ Always go back to TodayScheduleScreen
                    navController.popBackStack("today_schedule", inclusive = false)
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
