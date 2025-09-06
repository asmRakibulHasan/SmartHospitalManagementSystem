package com.example.smarthospitalmanagementsystem.data.dao

import androidx.room.*
import com.example.smarthospitalmanagementsystem.data.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {

    @Query("SELECT * FROM doctors")
    fun getAllDoctors(): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE email = :email")
    suspend fun getDoctorByEmail(email: String): DoctorEntity?

    @Query("SELECT * FROM doctors WHERE email = :email AND password = :password")
    suspend fun loginDoctor(email: String, password: String): DoctorEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDoctor(doctor: DoctorEntity): Long

    @Update
    suspend fun updateDoctor(doctor: DoctorEntity)

    @Delete
    suspend fun deleteDoctor(doctor: DoctorEntity)

    @Query("SELECT COUNT(*) FROM doctors")
    suspend fun getDoctorCount(): Int
}
