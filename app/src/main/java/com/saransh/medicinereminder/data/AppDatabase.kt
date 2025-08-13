package com.saransh.medicinereminder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saransh.medicinereminder.models.Medicine
import com.saransh.medicinereminder.models.DailySchedule

@Database(
    entities = [Medicine::class, DailySchedule::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun dailyScheduleDao(): DailyScheduleDao
}
