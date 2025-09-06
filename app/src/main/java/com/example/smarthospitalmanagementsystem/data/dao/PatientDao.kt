package com.example.smarthospitalmanagementsystem.data.dao

import androidx.room.*
import com.example.smarthospitalmanagementsystem.data.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE email = :email")
    suspend fun getPatientByEmail(email: String): PatientEntity?

    @Query("SELECT * FROM patients WHERE email = :email AND password = :password")
    suspend fun loginPatient(email: String, password: String): PatientEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPatient(patient: PatientEntity): Long

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    @Query("SELECT COUNT(*) FROM patients")
    suspend fun getPatientCount(): Int
}
