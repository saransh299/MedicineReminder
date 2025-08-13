package com.saransh.medicinereminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saransh.medicinereminder.data.MedicineRepository

class TodayScheduleViewModelFactory(
    private val repository: MedicineRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodayScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
