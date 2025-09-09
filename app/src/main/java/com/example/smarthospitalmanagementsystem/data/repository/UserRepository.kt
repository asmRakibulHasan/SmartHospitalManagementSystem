package com.example.smarthospitalmanagementsystem.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.smarthospitalmanagementsystem.data.dao.AppointmentDao
import kotlinx.coroutines.flow.Flow
import com.example.smarthospitalmanagementsystem.data.dao.PatientDao
import com.example.smarthospitalmanagementsystem.data.dao.DoctorDao
import com.example.smarthospitalmanagementsystem.data.entity.AppointmentEntity
import com.example.smarthospitalmanagementsystem.data.entity.PatientEntity
import com.example.smarthospitalmanagementsystem.data.entity.DoctorEntity

class UserRepository(
    private val patientDao: PatientDao,
    private val doctorDao: DoctorDao,
    val appointmentDao: AppointmentDao
) {

    // Flow for observing all patients
    val allPatients: Flow<List<PatientEntity>> = patientDao.getAllPatients()

    // Flow for observing all doctors
    val allDoctors: Flow<List<DoctorEntity>> = doctorDao.getAllDoctors()

    // Patient operations
    @WorkerThread
    suspend fun insertPatient(patient: PatientEntity): Boolean {
        return try {
            // Check if email already exists
            val existingPatient = patientDao.getPatientByEmail(patient.email)
            val existingDoctor = doctorDao.getDoctorByEmail(patient.email)

            if (existingPatient != null || existingDoctor != null) {
                Log.w("UserRepository", "Email already exists: ${patient.email}")
                false
            } else {
                val id = patientDao.insertPatient(patient)
                Log.d("UserRepository", "Patient inserted with ID: $id")
                true
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error inserting patient: ${e.message}")
            false
        }
    }

    @WorkerThread
    suspend fun insertDoctor(doctor: DoctorEntity): Boolean {
        return try {
            // Check if email already exists
            val existingPatient = patientDao.getPatientByEmail(doctor.email)
            val existingDoctor = doctorDao.getDoctorByEmail(doctor.email)

            if (existingPatient != null || existingDoctor != null) {
                Log.w("UserRepository", "Email already exists: ${doctor.email}")
                false
            } else {
                val id = doctorDao.insertDoctor(doctor)
                Log.d("UserRepository", "Doctor inserted with ID: $id")
                true
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error inserting doctor: ${e.message}")
            false
        }
    }

    @WorkerThread
    suspend fun loginPatient(email: String, password: String): PatientEntity? {
        return patientDao.loginPatient(email, password)
    }

    @WorkerThread
    suspend fun loginDoctor(email: String, password: String): DoctorEntity? {
        return doctorDao.loginDoctor(email, password)
    }

    @WorkerThread
    suspend fun getUserCounts(): Pair<Int, Int> {
        val patientCount = patientDao.getPatientCount()
        val doctorCount = doctorDao.getDoctorCount()
        return Pair(patientCount, doctorCount)
    }

    @WorkerThread
    suspend fun findUserByEmail(email: String): Pair<String?, String?> {
        val patient = patientDao.getPatientByEmail(email)
        if (patient != null) {
            return Pair("Patient", patient.name)
        }

        val doctor = doctorDao.getDoctorByEmail(email)
        if (doctor != null) {
            return Pair("Doctor", doctor.name)
        }

        return Pair(null, null)
    }

    @WorkerThread
    suspend fun insertAppointment(appointment: AppointmentEntity): Long {
        return appointmentDao.insertAppointment(appointment)
    }
}
