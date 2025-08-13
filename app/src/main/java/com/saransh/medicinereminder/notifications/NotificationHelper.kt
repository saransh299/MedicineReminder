package com.saransh.medicinereminder.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.saransh.medicinereminder.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "medicine_reminder_channel"
        const val CHANNEL_NAME = "Medicine Reminders"
        const val CHANNEL_DESC = "Notifications for medicine reminders"
    }

    private val notificationManager =
        NotificationManagerCompat.from(context)

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun notifyMedicineTime(medicineName: String, time: String) {
        val notificationId = System.currentTimeMillis().toInt()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Add this drawable in your res/drawable folder
            .setContentTitle("Medicine Reminder")
            .setContentText("You need to take $medicineName at $time")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
