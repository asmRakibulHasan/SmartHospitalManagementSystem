package com.example.smarthospitalmanagementsystem.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.smarthospitalmanagementsystem.data.entity.AppointmentEntity

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointments ORDER BY bookingDate DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE patientName = :patientName ORDER BY bookingDate DESC")
    fun getAppointmentsByPatient(patientName: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE doctorName = :doctorName ORDER BY bookingDate DESC")
    fun getAppointmentsByDoctor(doctorName: String): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity): Long

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Delete
    suspend fun deleteAppointment(appointment: AppointmentEntity)

    @Query("SELECT COUNT(*) FROM appointments")
    suspend fun getAppointmentCount(): Int
}
