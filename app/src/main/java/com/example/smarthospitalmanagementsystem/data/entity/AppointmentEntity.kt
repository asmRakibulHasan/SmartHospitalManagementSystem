package com.example.smarthospitalmanagementsystem.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientName: String,
    val doctorName: String,
    val doctorSpecialty: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val chamberLocation: String,
    val bookingDate: Long = System.currentTimeMillis(),
    val status: String = "Booked" // Booked, Completed, Cancelled
)
