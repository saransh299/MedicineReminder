package com.saransh.medicinereminder.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "medicine")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val doctor: String? = null,
    val dosage: String,
    val instructions: String? = null,
    val timesPerDayJson: String, // JSON array of "HH:mm"
    val repeatDaysJson: String,  // JSON array like ["Mon","Tue"]
    val startDate: Long,
    val endDate: Long
)
