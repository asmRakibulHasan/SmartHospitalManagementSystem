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
import com.example.smarthospitalmanagementsystem.databinding.FragmentLoginBinding
import com.example.smarthospitalmanagementsystem.viewmodel.DoctorPatientViewModel
import com.example.smarthospitalmanagementsystem.viewmodel.LoginState
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel across fragments
    private val viewModel: DoctorPatientViewModel by activityViewModels()

    // Track current selection
    private var isPatientSelected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup click listeners
        setupClickListeners()

        // Set initial state (Patient selected by default)
        setPatientSelected()

        // Observe login state
        observeLoginState()
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

        // Login button listener
        binding.btnLogin.setOnClickListener {
            handleLogin()
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
        binding.tvLoginTitle.text = "Patients Login"

        Log.d("LoginFragment", "Patient toggle selected")
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
        binding.tvLoginTitle.text = "Doctor Login"

        Log.d("LoginFragment", "Doctor toggle selected")
    }

    private fun handleLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Clear previous errors
        binding.etUsername.error = null
        binding.etPassword.error = null

        // Validate inputs
        if (validateInputs(username, password)) {
            // Show loading state
            setLoginButtonLoading(true)

            // Perform login
            performLogin(username, password)
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        when {
            username.isEmpty() -> {
                binding.etUsername.error = "Email is required"
                binding.etUsername.requestFocus()
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(username).matches() -> {
                binding.etUsername.error = "Please enter a valid email"
                binding.etUsername.requestFocus()
                return false
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password is required"
                binding.etPassword.requestFocus()
                return false
            }
            password.length < 6 -> {
                binding.etPassword.error = "Password must be at least 6 characters"
                binding.etPassword.requestFocus()
                return false
            }
        }
        return true
    }

    private fun performLogin(username: String, password: String) {
        Log.d("LoginFragment", "=== Login Attempt ===")
        Log.d("LoginFragment", "Selected Type: ${if (isPatientSelected) "Patient" else "Doctor"}")
        Log.d("LoginFragment", "Email: $username")
        Log.d("LoginFragment", "====================")

        // Call ViewModel login method
        viewModel.login(username, password)
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewModel.loginState.collect { loginState ->
                when (loginState) {
                    is LoginState.Idle -> {
                        setLoginButtonLoading(false)
                    }
                    is LoginState.Loading -> {
                        setLoginButtonLoading(true)
                    }
                    is LoginState.Success -> {
                        setLoginButtonLoading(false)
                        handleLoginSuccess(loginState.userType, loginState.userName)
                    }
                    is LoginState.Error -> {
                        setLoginButtonLoading(false)
                        handleLoginError(loginState.message)
                    }
                }
            }
        }
    }

    private fun handleLoginSuccess(userType: String, userName: String) {
        Log.d("LoginFragment", "Login successful - Type: $userType, Name: $userName")

        // Show success toast
        Toast.makeText(requireContext(), "Welcome, $userName!", Toast.LENGTH_SHORT).show()

        // Navigate based on user type
        when (userType.lowercase()) {
            "patient" -> {
                Log.d("LoginFragment", "Navigating to PatientFragment")
                findNavController().navigate(R.id.action_loginFragment_to_patientFragment)
            }
            "doctor" -> {
                Log.d("LoginFragment", "Navigating to DoctorFragment")
                findNavController().navigate(R.id.action_loginFragment_to_doctorFragment)
            }
            else -> {
                Log.e("LoginFragment", "Unknown user type: $userType")
                Toast.makeText(requireContext(), "Unknown user type", Toast.LENGTH_SHORT).show()
            }
        }

        // Clear form fields after successful login
        clearFormFields()

        // Reset login state
        viewModel.resetLoginState()
    }

    private fun handleLoginError(errorMessage: String) {
        Log.w("LoginFragment", "Login failed: $errorMessage")

        // Show error toast
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()

        // Set error on appropriate field
        when {
            errorMessage.contains("email", ignoreCase = true) -> {
                binding.etUsername.error = errorMessage
                binding.etUsername.requestFocus()
            }
            errorMessage.contains("password", ignoreCase = true) -> {
                binding.etPassword.error = errorMessage
                binding.etPassword.requestFocus()
            }
            else -> {
                // General error, focus on email field
                binding.etUsername.error = errorMessage
                binding.etUsername.requestFocus()
            }
        }

        // Reset login state
        viewModel.resetLoginState()
    }

    private fun setLoginButtonLoading(isLoading: Boolean) {
        binding.btnLogin.apply {
            isEnabled = !isLoading
            text = if (isLoading) "Logging in..." else "Login"
            alpha = if (isLoading) 0.7f else 1.0f
        }
    }

    private fun clearFormFields() {
        binding.etUsername.setText("")
        binding.etPassword.setText("")
        binding.etUsername.clearFocus()
        binding.etPassword.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
