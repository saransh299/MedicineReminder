package com.saransh.medicinereminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.saransh.medicinereminder.data.AppDatabase
import com.saransh.medicinereminder.data.MedicineRepository
import com.saransh.medicinereminder.ui.theme.MedicineReminderTheme
import com.saransh.medicinereminder.navigation.NavGraph


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create database and repository
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, // ✅ Corrected to use AppDatabase
            "medicine_db"
        ).build()
        val repository = MedicineRepository(database)

        setContent {
            MedicineReminderTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    repository = repository
                )
            }
        }
    }
}
