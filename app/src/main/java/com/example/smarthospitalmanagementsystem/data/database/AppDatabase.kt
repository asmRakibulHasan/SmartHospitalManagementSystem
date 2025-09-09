package com.example.smarthospitalmanagementsystem.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.smarthospitalmanagementsystem.data.entity.PatientEntity
import com.example.smarthospitalmanagementsystem.data.entity.DoctorEntity
import com.example.smarthospitalmanagementsystem.data.entity.AppointmentEntity
import com.example.smarthospitalmanagementsystem.data.dao.PatientDao
import com.example.smarthospitalmanagementsystem.data.dao.DoctorDao
import com.example.smarthospitalmanagementsystem.data.dao.AppointmentDao

@Database(
    entities = [PatientEntity::class, DoctorEntity::class, AppointmentEntity::class],
    version = 2, // Updated version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun doctorDao(): DoctorDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hospital_management_database"
                )
                    .fallbackToDestructiveMigration() // For demo purposes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
