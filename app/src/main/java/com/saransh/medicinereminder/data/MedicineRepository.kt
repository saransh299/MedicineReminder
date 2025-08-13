package com.saransh.medicinereminder.data

import com.saransh.medicinereminder.models.DailySchedule
import kotlinx.coroutines.flow.Flow

class MedicineRepository(private val database: AppDatabase) {

    private val dailyScheduleDao = database.dailyScheduleDao()

    fun getAllDailySchedules(): Flow<List<DailySchedule>> {
        return dailyScheduleDao.getAllSchedules()
    }

    fun getScheduleForDate(date: Long): Flow<List<DailySchedule>> {
        return dailyScheduleDao.getScheduleForDate(date)
    }

    suspend fun insertDailySchedule(schedule: DailySchedule) {
        dailyScheduleDao.insert(schedule)
    }

    suspend fun updateDailySchedule(schedule: DailySchedule) {
        dailyScheduleDao.update(schedule)
    }

    suspend fun deleteDailySchedule(schedule: DailySchedule) {
        dailyScheduleDao.delete(schedule)
    }
}
