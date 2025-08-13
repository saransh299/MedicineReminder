package com.saransh.medicinereminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.saransh.medicinereminder.data.AppDatabase
import com.saransh.medicinereminder.data.MedicineRepository
import com.saransh.medicinereminder.navigation.NavGraph
import com.saransh.medicinereminder.ui.theme.MedicineReminderTheme
import com.saransh.medicinereminder.viewmodel.TodayScheduleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Build database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "medicine_db"
        ).build()

        // Create repository
        val repository = MedicineRepository(db)

        // Manually create ViewModel
        val todayScheduleViewModel = TodayScheduleViewModel(repository)

        setContent {
            MedicineReminderTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        todayScheduleViewModel = todayScheduleViewModel
                    )
                }
            }
        }
    }
}
