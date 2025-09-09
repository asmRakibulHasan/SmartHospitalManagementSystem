package com.example.smarthospitalmanagementsystem.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.smarthospitalmanagementsystem.data.database.AppDatabase
import com.example.smarthospitalmanagementsystem.data.repository.UserRepository
import com.example.smarthospitalmanagementsystem.data.entity.PatientEntity
import com.example.smarthospitalmanagementsystem.data.entity.DoctorEntity

// Login states
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userType: String, val userName: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class DoctorPatientViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    // Login state
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // Registration state
    private val _registrationState = MutableStateFlow<Boolean?>(null)
    val registrationState: StateFlow<Boolean?> = _registrationState.asStateFlow()

    private val _lastUserName = MutableStateFlow<String>("User")
    val lastUserName: StateFlow<String> = _lastUserName.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UserRepository(database.patientDao(), database.doctorDao())
        Log.d("DoctorPatientViewModel", "ViewModel initialized with Room database")

        // Add default users for testing
        addDefaultUsers()
    }

    // Add default users for testing purposes
    private fun addDefaultUsers() {
        viewModelScope.launch {
            // Check if default users already exist
            val existingPatient = repository.findUserByEmail("p@g.c")
            val existingDoctor = repository.findUserByEmail("d@g.c")

            if (existingPatient.first == null) {
                addPatient("Test Patient", "p@g.c", "123456")
            }

            if (existingDoctor.first == null) {
                addDoctor("Dr. Test", "d@g.c", "123456")
            }
        }
    }

    /**
     * Register a new patient
     */
    fun addPatient(name: String, email: String, password: String) {
        viewModelScope.launch {
            val patient = PatientEntity(
                name = name,
                email = email,
                password = password
            )

            val success = repository.insertPatient(patient)
            _registrationState.value = success

            if (success) {
                Log.d("DoctorPatientViewModel", "Patient registered successfully: $name ($email)")
            } else {
                Log.w("DoctorPatientViewModel", "Patient registration failed: Email already exists - $email")
            }
        }
    }

    /**
     * Register a new doctor
     */
    fun addDoctor(name: String, email: String, password: String) {
        viewModelScope.launch {
            val doctor = DoctorEntity(
                name = name,
                email = email,
                password = password
            )

            val success = repository.insertDoctor(doctor)
            _registrationState.value = success

            if (success) {
                Log.d("DoctorPatientViewModel", "Doctor registered successfully: $name ($email)")
            } else {
                Log.w("DoctorPatientViewModel", "Doctor registration failed: Email already exists - $email")
            }
        }
    }

    /**
     * Login functionality - checks both patient and doctor tables
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            // Validate input
            if (!isValidLoginInput(email, password)) {
                _loginState.value = LoginState.Error("Please enter valid email and password")
                return@launch
            }

            Log.d("DoctorPatientViewModel", "Attempting login for: $email")

            try {
                // Check in patient table first
                val patient = repository.loginPatient(email, password)
                if (patient != null) {
                    Log.d("DoctorPatientViewModel", "Patient login successful: ${patient.name}")
                    _lastUserName.value = patient.name
                    _loginState.value = LoginState.Success("Patient", patient.name)
                    return@launch
                }

                // Check in doctor table
                val doctor = repository.loginDoctor(email, password)
                if (doctor != null) {
                    Log.d("DoctorPatientViewModel", "Doctor login successful: ${doctor.name}")
                    _lastUserName.value = doctor.name
                    _loginState.value = LoginState.Success("Doctor", doctor.name)
                    return@launch
                }

                // No match found
                Log.w("DoctorPatientViewModel", "Login failed: Invalid credentials for $email")
                _loginState.value = LoginState.Error("Invalid email or password")

            } catch (e: Exception) {
                Log.e("DoctorPatientViewModel", "Login error: ${e.message}")
                _loginState.value = LoginState.Error("Login failed. Please try again.")
            }
        }
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
     * Reset registration state
     */
    fun resetRegistrationState() {
        _registrationState.value = null
    }

    /**
     * Get user count
     */
    fun getUserCounts(callback: (Pair<Int, Int>) -> Unit) {
        viewModelScope.launch {
            val counts = repository.getUserCounts()
            callback(counts)
        }
    }

    /**
     * Find user by email (returns type and name)
     */
    fun findUserByEmail(email: String, callback: (Pair<String?, String?>) -> Unit) {
        viewModelScope.launch {
            val result = repository.findUserByEmail(email)
            callback(result)
        }
    }
}
