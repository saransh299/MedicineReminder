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
import com.saransh.medicinereminder.screens.MedicineDetailScreen
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
        // TODAY SCREEN
        composable("today_schedule") {
            TodayScheduleScreen(
                todaySchedules = todaySchedulesState.value,
                todayScheduleViewModel = todayScheduleViewModel,
                navController = navController,
                onAddClick = { navController.navigate("add_schedule_basic_info") }
            )
        }

        // BASIC INFO SCREEN
        composable("add_schedule_basic_info") {
            AddScheduleBasicInfoScreen { medicineName, doctorName, freqCount, freqType ->
                val m = Uri.encode(medicineName)
                val d = Uri.encode(doctorName ?: "")
                val t = Uri.encode(freqType)
                navController.navigate("add_schedule_time/$m/$d/$freqCount/$t")
            }
        }

        // TIME SELECTION SCREEN
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
                    navController.popBackStack("today_schedule", inclusive = false)
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // MEDICINE DETAIL SCREEN
        composable(
            route = "medicine_detail/{scheduleId}",
            arguments = listOf(navArgument("scheduleId") { type = NavType.LongType })
        ) { backStackEntry ->

            val scheduleId = backStackEntry.arguments?.getLong("scheduleId") ?: 0L

            // ✅ Find the clicked schedule
            val schedule = todaySchedulesState.value.find { it.id.toLong() == scheduleId }

            schedule?.let {
                // ✅ Pass all schedules for the same medicine to the detail screen
                val allSchedulesForMedicine = todaySchedulesState.value.filter { dose ->
                    dose.taskName == schedule.taskName
                }

                MedicineDetailScreen(
                    schedule = it,
                    allSchedulesForMedicine = allSchedulesForMedicine,
                    onDelete = {
                        todayScheduleViewModel.deleteSchedule(it)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
