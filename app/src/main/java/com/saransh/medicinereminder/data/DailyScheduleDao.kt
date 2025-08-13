package com.saransh.medicinereminder.data

import androidx.room.*
import com.saransh.medicinereminder.models.DailySchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyScheduleDao {

    @Query("SELECT * FROM daily_schedule ORDER BY startTime ASC")
    fun getAllSchedules(): Flow<List<DailySchedule>>

    @Query("SELECT * FROM daily_schedule WHERE date = :date ORDER BY startTime ASC")
    fun getScheduleForDate(date: Long): Flow<List<DailySchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: DailySchedule)

    @Update
    suspend fun update(schedule: DailySchedule)

    @Delete
    suspend fun delete(schedule: DailySchedule)
}
