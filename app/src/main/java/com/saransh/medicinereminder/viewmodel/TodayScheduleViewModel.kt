package com.saransh.medicinereminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saransh.medicinereminder.data.MedicineRepository
import com.saransh.medicinereminder.models.DailySchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class TodayScheduleViewModel(
    private val repository: MedicineRepository
) : ViewModel() {

    private val _todaySchedules = MutableStateFlow<List<DailySchedule>>(emptyList())
    val todaySchedules: StateFlow<List<DailySchedule>> = _todaySchedules

    init {
        loadTodaySchedules()
    }

    private fun loadTodaySchedules() {
        val todayStart = getTodayMidnightEpoch()
        viewModelScope.launch {
            repository.getScheduleForDate(todayStart).collectLatest {
                _todaySchedules.value = it
            }
        }
    }

    private fun getTodayMidnightEpoch(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    suspend fun updateScheduleStatus(schedule: DailySchedule, status: String) {
        val updated = schedule.copy(status = status, actionTime = System.currentTimeMillis())
        repository.updateDailySchedule(updated)
    }

    // ✅ NEW: Add a schedule
    fun addSchedule(schedule: DailySchedule) {
        viewModelScope.launch {
            repository.insertDailySchedule(schedule)
        }
    }

    // ✅ NEW: Delete a schedule
    fun deleteSchedule(schedule: DailySchedule) {
        viewModelScope.launch {
            repository.deleteDailySchedule(schedule)
        }
    }
}
