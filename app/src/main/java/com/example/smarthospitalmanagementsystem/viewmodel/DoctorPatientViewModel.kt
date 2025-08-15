package com.example.smarthospitalmanagementsystem.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data classes for User entities
data class Patient(
    val name: String,
    val email: String,
    val password: String,
    val registrationDate: Long = System.currentTimeMillis()
)

data class Doctor(
    val name: String,
    val email: String,
    val password: String,
    val registrationDate: Long = System.currentTimeMillis()
)

// Login states
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userType: String, val userName: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class DoctorPatientViewModel : ViewModel() {

    // Private mutable lists
    private val _patientList = MutableStateFlow<List<Patient>>(emptyList())
    private val _doctorList = MutableStateFlow<List<Doctor>>(emptyList())

    // Public read-only access
    val patientList: StateFlow<List<Patient>> = _patientList.asStateFlow()
    val doctorList: StateFlow<List<Doctor>> = _doctorList.asStateFlow()

    // Login state
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    init {
        Log.d("DoctorPatientViewModel", "ViewModel initialized")
        // Add some default users for testing
        addDefaultUsers()
    }

    // Add default users for testing purposes
    private fun addDefaultUsers() {
        addPatient("Test Patient", "patient@test.com", "123456")
        addDoctor("Dr. Test", "doctor@test.com", "123456")
    }

    /**
     * Register a new patient
     */
    fun addPatient(name: String, email: String, password: String): Boolean {
        // Check if email already exists
        if (isEmailExists(email)) {
            Log.w("DoctorPatientViewModel", "Patient registration failed: Email already exists - $email")
            return false
        }

        val newPatient = Patient(name, email, password)
        val currentList = _patientList.value.toMutableList()
        currentList.add(newPatient)
        _patientList.value = currentList

        Log.d("DoctorPatientViewModel", "Patient registered successfully: $name ($email)")
        Log.d("DoctorPatientViewModel", "Total patients: ${_patientList.value.size}")

        return true
    }

    /**
     * Register a new doctor
     */
    fun addDoctor(name: String, email: String, password: String): Boolean {
        // Check if email already exists
        if (isEmailExists(email)) {
            Log.w("DoctorPatientViewModel", "Doctor registration failed: Email already exists - $email")
            return false
        }

        val newDoctor = Doctor(name, email, password)
        val currentList = _doctorList.value.toMutableList()
        currentList.add(newDoctor)
        _doctorList.value = currentList

        Log.d("DoctorPatientViewModel", "Doctor registered successfully: $name ($email)")
        Log.d("DoctorPatientViewModel", "Total doctors: ${_doctorList.value.size}")

        return true
    }

    /**
     * Check if email already exists in either patient or doctor list
     */
    private fun isEmailExists(email: String): Boolean {
        val patientExists = _patientList.value.any { it.email.equals(email, ignoreCase = true) }
        val doctorExists = _doctorList.value.any { it.email.equals(email, ignoreCase = true) }
        return patientExists || doctorExists
    }

    /**
     * Login functionality - checks both patient and doctor lists
     */
    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading

        // Validate input
        if (!isValidLoginInput(email, password)) {
            _loginState.value = LoginState.Error("Please enter valid email and password")
            return
        }

        Log.d("DoctorPatientViewModel", "Attempting login for: $email")

        // Check in patient list first
        val patient = _patientList.value.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        if (patient != null) {
            Log.d("DoctorPatientViewModel", "Patient login successful: ${patient.name}")
            _loginState.value = LoginState.Success("Patient", patient.name)
            return
        }

        // Check in doctor list
        val doctor = _doctorList.value.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        if (doctor != null) {
            Log.d("DoctorPatientViewModel", "Doctor login successful: ${doctor.name}")
            _loginState.value = LoginState.Success("Doctor", doctor.name)
            return
        }

        // No match found
        Log.w("DoctorPatientViewModel", "Login failed: Invalid credentials for $email")
        _loginState.value = LoginState.Error("Invalid email or password")
    }

    /**
     * Validate login input
     */
    private fun isValidLoginInput(email: String, password: String): Boolean {
        return email.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                password.isNotEmpty()
    }

    /**
     * Reset login state
     */
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    /**
     * Get all registered users (for admin purposes)
     */
    fun getAllUsers(): Pair<List<Patient>, List<Doctor>> {
        return Pair(_patientList.value, _doctorList.value)
    }

    /**
     * Get user count
     */
    fun getUserCounts(): Pair<Int, Int> {
        return Pair(_patientList.value.size, _doctorList.value.size)
    }

    /**
     * Find user by email (returns type and name)
     */
    fun findUserByEmail(email: String): Pair<String?, String?> {
        val patient = _patientList.value.find { it.email.equals(email, ignoreCase = true) }
        if (patient != null) {
            return Pair("Patient", patient.name)
        }

        val doctor = _doctorList.value.find { it.email.equals(email, ignoreCase = true) }
        if (doctor != null) {
            return Pair("Doctor", doctor.name)
        }

        return Pair(null, null)
    }

    /**
     * Clear all data (for testing purposes)
     */
    fun clearAllData() {
        _patientList.value = emptyList()
        _doctorList.value = emptyList()
        _loginState.value = LoginState.Idle
        Log.d("DoctorPatientViewModel", "All data cleared")
    }
}
