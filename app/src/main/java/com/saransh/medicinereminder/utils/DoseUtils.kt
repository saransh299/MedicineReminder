package com.saransh.medicinereminder.utils

import android.util.Log
import com.saransh.medicinereminder.models.DailySchedule
import java.text.SimpleDateFormat
import java.util.*

fun getNextDose(doses: List<DailySchedule>): DailySchedule? {
    val now = Calendar.getInstance()
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

    Log.d("DoseUtils", "Current time: ${sdf.format(now.time)}")

    val nextDose = doses.sortedBy { it.id }.firstOrNull { dose ->
        try {
            val parsed = sdf.parse(dose.startTime) ?: return@firstOrNull false
            val doseCal = Calendar.getInstance().apply {
                time = now.time // Start with today
                set(Calendar.HOUR_OF_DAY, parsed.hours)
                set(Calendar.MINUTE, parsed.minutes)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            Log.d("DoseUtils", "Dose: ${dose.startTime}, doseCal: ${doseCal.time}, after now? ${doseCal.after(now)}")
            doseCal.after(now)
        } catch (e: Exception) {
            Log.d("DoseUtils", "Error parsing dose: ${dose.startTime}, ${e.message}")
            false
        }
    }

    Log.d("DoseUtils", "Next dose: ${nextDose?.startTime}")
    return nextDose
}
