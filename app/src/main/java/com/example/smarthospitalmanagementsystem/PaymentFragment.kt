package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smarthospitalmanagementsystem.databinding.FragmentPaymentBinding
import com.example.smarthospitalmanagementsystem.viewmodel.DoctorPatientViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel
    private val viewModel: DoctorPatientViewModel by activityViewModels()

    // Demo appointment details
    private val doctorName = "Dr. Sarah Mitchell"
    private val doctorSpecialty = "Ophthalmologist"
    private val chamberLocation = "City Medical Center"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSuccessScreen()
        setupClickListeners()
        observeUserData()
    }

    private fun setupSuccessScreen() {
        // Set demo appointment details
        binding.tvDoctorName.text = doctorName
        binding.tvSpecialty.text = doctorSpecialty
        binding.tvLocation.text = chamberLocation

        // Set current date and time as demo
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.tvDate.text = currentDate
        binding.tvTime.text = "10:00 AM" // Demo time

        // Generate booking reference
        val bookingRef = "APT${System.currentTimeMillis().toString().takeLast(5)}"
        binding.tvBookingRef.text = "Booking Reference: #$bookingRef"

        Log.d("PaymentFragment", "Success screen setup completed")
        Log.d("PaymentFragment", "Booking Reference: $bookingRef")
    }

    private fun observeUserData() {
        lifecycleScope.launch {
            viewModel.lastUserName.collect { userName ->
                // Set patient name from logged-in user
                binding.tvPatientName.text = userName
                Log.d("PaymentFragment", "Patient name set: $userName")
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnHome.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        try {
            Log.d("PaymentFragment", "Navigating to Home (UserFragment)")

            // Navigate to UserFragment and clear back stack
            findNavController().navigate(
                R.id.action_paymentFragment_to_userFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.userFragment, true)
                    .build()
            )

        } catch (e: Exception) {
            Log.e("PaymentFragment", "Navigation failed: ${e.message}")
        }
    }

    // Method to set appointment details if passed from previous fragment
    fun setAppointmentDetails(
        patientName: String,
        doctorName: String,
        specialty: String,
        date: String,
        time: String,
        location: String
    ) {

        binding.tvPatientName.text = patientName
        binding.tvDoctorName.text = doctorName
        binding.tvSpecialty.text = specialty
        binding.tvDate.text = date
        binding.tvTime.text = time
        binding.tvLocation.text = location

        Log.d("PaymentFragment", "Appointment details updated")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
