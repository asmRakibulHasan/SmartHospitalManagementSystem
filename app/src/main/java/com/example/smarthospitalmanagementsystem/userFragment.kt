package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smarthospitalmanagementsystem.databinding.FragmentUserBinding
import com.example.smarthospitalmanagementsystem.viewmodel.DoctorPatientViewModel
import com.example.smarthospitalmanagementsystem.viewmodel.LoginState
import kotlinx.coroutines.launch

class userFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel across fragments
    private val viewModel: DoctorPatientViewModel by activityViewModels()

    // Store current user info
    private var currentUserName: String = ""
    private var currentUserType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup click listeners
        setupClickListeners()

        // Observe login state to get current user info
        observeLoginState()
    }

    private fun setupClickListeners() {
        // Home button click
//        binding.btnHome.setOnClickListener {
//            Log.d("UserFragment", "Home button clicked")
//            // Navigate back to home or previous screen
//            findNavController().navigateUp()
//        }

        // Doctor List button click
        binding.btnDoctorList.setOnClickListener {
            Log.d("UserFragment", "Doctor List button clicked")
            navigateToDoctorList()
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
//            viewModel.loginState.collect { loginState ->
//                when (loginState) {
//                    is LoginState.Success -> {
//                        // Get current logged-in user info
//                        currentUserName = loginState.userName
//                        currentUserType = loginState.userType
//                        setWelcomeMessage(currentUserName)
//
//                        Log.d("UserFragment", "Current user: $currentUserName ($currentUserType)")
//                    }
//                    is LoginState.Idle -> {
//                        // No user logged in, show default message
//                        setWelcomeMessage(viewModel.lastUserName.value)
//                        Log.d("UserFragment", "LoginState.Idle")
//                    }
//                    else -> {
//                        // Handle other states if needed
//                    }
//                }
//            }
            setWelcomeMessage(viewModel.lastUserName.value)
        }
    }

    private fun setWelcomeMessage(userName: String) {
        val welcomeText = "Welcome $userName!"
        binding.tvWelcomeMessage.text = welcomeText
        Log.d("UserFragment", "Welcome message set: $welcomeText")
    }

    private fun navigateToDoctorList() {
        try {
            // Navigate to patient fragment (which shows doctor list)
            findNavController().navigate(R.id.action_userFragment_to_patientFragment)
            Log.d("UserFragment", "Navigating to PatientFragment (Doctor List)")
        } catch (e: Exception) {
            Log.e("UserFragment", "Navigation failed: ${e.message}")
            Toast.makeText(
                requireContext(),
                "Navigation failed. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Optional: Method to manually set user info if needed
    fun setUserInfo(userName: String, userType: String) {
        currentUserName = userName
        currentUserType = userType
        setWelcomeMessage(userName)

        Log.d("UserFragment", "User info manually set: $userName ($userType)")
    }

    // Optional: Method to get current user info
    fun getCurrentUserInfo(): Pair<String, String> {
        return Pair(currentUserName, currentUserType)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
