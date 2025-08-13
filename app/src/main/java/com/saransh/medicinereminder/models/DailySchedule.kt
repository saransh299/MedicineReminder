package com.saransh.medicinereminder.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "daily_schedule")
data class DailySchedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val date: Long, // store date as epoch millis for filtering
    val dayOfWeek: String, // e.g., "Monday"
    val startTime: String, // e.g., "09:00"
    val endTime: String,   // e.g., "17:00"
    val taskName: String,  // e.g., "Take medicine" or "Work"
    val notes: String? = null,
    val status: String = "pending",    // Added: "pending", "taken", "missed", "snoozed"
    val actionTime: Long? = null
)
