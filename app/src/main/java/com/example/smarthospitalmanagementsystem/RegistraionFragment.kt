package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smarthospitalmanagementsystem.databinding.FragmentRegistraionBinding
import com.example.smarthospitalmanagementsystem.viewmodel.DoctorPatientViewModel
import kotlinx.coroutines.launch

class RegistraionFragment : Fragment() {

    private var _binding: FragmentRegistraionBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel across fragments
    private val viewModel: DoctorPatientViewModel by activityViewModels()

    // Track current selection
    private var isPatientSelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistraionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup click listeners
        setupClickListeners()

        // Set initial state (Patient selected by default)
        setPatientSelected()

        // Observe registration state
        observeRegistrationState()
    }

    private fun setupClickListeners() {
        // Toggle button listeners
        binding.btnTogglePatients.setOnClickListener {
            if (!isPatientSelected) {
                setPatientSelected()
            }
        }

        binding.btnToggleDoctor.setOnClickListener {
            if (isPatientSelected) {
                setDoctorSelected()
            }
        }

        // Proceed button listener
        binding.btnProceed.setOnClickListener {
            handleRegistration()
        }
    }

    private fun setPatientSelected() {
        isPatientSelected = true

        // Update button styles
        binding.btnTogglePatients.apply {
            setBackgroundResource(R.drawable.toggle_button_selected)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.toggle_selected_text))
        }

        binding.btnToggleDoctor.apply {
            setBackgroundResource(R.drawable.toggle_button_unselected)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.toggle_text_unselected))
        }

        // Update title
        binding.tvRegistrationTitle.text = "Patients Registration"

        Log.d("RegistrationFragment", "Patient toggle selected")
    }

    private fun setDoctorSelected() {
        isPatientSelected = false

        // Update button styles
        binding.btnToggleDoctor.apply {
            setBackgroundResource(R.drawable.toggle_button_selected)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.toggle_selected_text))
        }

        binding.btnTogglePatients.apply {
            setBackgroundResource(R.drawable.toggle_button_unselected)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.toggle_text_unselected))
        }

        // Update title
        binding.tvRegistrationTitle.text = "Doctor Registration"

        Log.d("RegistrationFragment", "Doctor toggle selected")
    }

    private fun observeRegistrationState() {
        lifecycleScope.launch {
            viewModel.registrationState.collect { success ->
                when (success) {
                    true -> {
                        val registrationType = if (isPatientSelected) "Patient" else "Doctor"
                        val successMessage = "$registrationType registration successful"
                        Toast.makeText(requireContext(), successMessage, Toast.LENGTH_LONG).show()

                        // Log current counts for verification
                        viewModel.getUserCounts { (patientCount, doctorCount) ->
                            Log.d("RegistrationFragment", "Current counts - Patients: $patientCount, Doctors: $doctorCount")
                        }

                        // Clear form fields
                        clearFormFields()

                        // Navigate back
                        findNavController().navigateUp()

                        // Reset registration state
                        viewModel.resetRegistrationState()
                    }
                    false -> {
                        // Email already exists
                        binding.etEmail.error = "Email already exists"
                        binding.etEmail.requestFocus()
                        Toast.makeText(requireContext(), "Email already exists. Please use a different email.", Toast.LENGTH_LONG).show()
                        Log.w("RegistrationFragment", "Registration failed: Email already exists")

                        // Reset registration state
                        viewModel.resetRegistrationState()
                    }
                    null -> {
                        // Initial state, do nothing
                    }
                }
            }
        }
    }

    private fun handleRegistration() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Clear previous errors
        binding.etName.error = null
        binding.etEmail.error = null
        binding.etPassword.error = null

        // Validate inputs
        if (validateInputs(name, email, password)) {
            // Show loading state
            setRegistrationButtonLoading(true)

            // Process registration
            processRegistration(name, email, password)
        }
    }

    private fun validateInputs(name: String, email: String, password: String): Boolean {
        // Check if name is empty
        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            binding.etName.requestFocus()
            return false
        }

        // Check if email is empty
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            binding.etEmail.requestFocus()
            return false
        }

        // Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return false
        }

        // Check if password is empty
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            binding.etPassword.requestFocus()
            return false
        }

        // Check password length
        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun processRegistration(name: String, email: String, password: String) {
        val registrationType = if (isPatientSelected) "Patient" else "Doctor"

        // Log the registration details
        Log.d("RegistrationFragment", "=== Registration Details ===")
        Log.d("RegistrationFragment", "Type: $registrationType")
        Log.d("RegistrationFragment", "Name: $name")
        Log.d("RegistrationFragment", "Email: $email")
        Log.d("RegistrationFragment", "Password: $password")
        Log.d("RegistrationFragment", "============================")

        // Add to ViewModel (this will trigger the observer)
        if (isPatientSelected) {
            viewModel.addPatient(name, email, password)
        } else {
            viewModel.addDoctor(name, email, password)
        }
    }

    private fun setRegistrationButtonLoading(isLoading: Boolean) {
        binding.btnProceed.apply {
            isEnabled = !isLoading
            text = if (isLoading) "Processing..." else "Proceed"
            alpha = if (isLoading) 0.7f else 1.0f
        }
    }

    private fun clearFormFields() {
        binding.etName.setText("")
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        binding.etName.clearFocus()
        binding.etEmail.clearFocus()
        binding.etPassword.clearFocus()

        // Reset button state
        setRegistrationButtonLoading(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
