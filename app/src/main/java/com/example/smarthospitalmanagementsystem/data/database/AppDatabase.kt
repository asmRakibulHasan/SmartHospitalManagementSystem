package com.example.smarthospitalmanagementsystem.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.smarthospitalmanagementsystem.data.entity.PatientEntity
import com.example.smarthospitalmanagementsystem.data.entity.DoctorEntity
import com.example.smarthospitalmanagementsystem.data.dao.PatientDao
import com.example.smarthospitalmanagementsystem.data.dao.DoctorDao

@Database(
    entities = [PatientEntity::class, DoctorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun doctorDao(): DoctorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hospital_management_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
