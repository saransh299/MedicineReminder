package com.saransh.medicinereminder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saransh.medicinereminder.models.Medicine

@Database(
    entities = [Medicine::class],
    version = 1,
    exportSchema = false
)
abstract class MedicineDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
}
