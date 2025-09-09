package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smarthospitalmanagementsystem.databinding.FragmentDoctorProfileBinding

class DoctorProfileFragment : Fragment() {

    private var _binding: FragmentDoctorProfileBinding? = null
    private val binding get() = _binding!!

    // Demo doctor data - hardcoded for demo purposes
    private val doctorName = "Dr. Sarah Mitchell"
    private val doctorSpecialty = "Ophthalmologist"
    private val doctorRating = 4.2f
    private val doctorImageRes = R.drawable.doctor_photo_background

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup doctor profile data with demo data
        setupDoctorProfile()

        // Setup click listeners
        setupClickListeners()
    }

    private fun setupDoctorProfile() {
        // Set demo doctor image
        binding.imgDoctor.setImageResource(doctorImageRes)

        // Set demo doctor name
        binding.tvDoctorName.text = doctorName

        // Set demo doctor specialty
        binding.tvDoctorSpecialty.text = doctorSpecialty

        // Set demo doctor rating
        binding.tvRating.text = doctorRating.toString()

        // Update stars based on rating
        updateStarRating(doctorRating)

        Log.d("DoctorProfileFragment", "Doctor profile setup completed with demo data")
        Log.d("DoctorProfileFragment", "Name: $doctorName, Specialty: $doctorSpecialty, Rating: $doctorRating")
    }

    private fun updateStarRating(rating: Float) {
        // Demo implementation - keeping static stars as defined in XML
        Log.d("DoctorProfileFragment", "Rating updated to: $rating")
    }

    private fun setupClickListeners() {
        // Book Now button click listener
        binding.btnBookNow.setOnClickListener {
            navigateToBooking()
        }
    }

    private fun navigateToBooking() {
        try {
            Log.d("DoctorProfileFragment", "Navigating to BookingFragment")
            Log.d("DoctorProfileFragment", "Doctor: $doctorName")

            // Navigate to BookingFragment - no data passing needed for demo
            findNavController().navigate(R.id.action_doctorProfileFragment_to_bookingFragment)


        } catch (e: Exception) {
            Log.e("DoctorProfileFragment", "Navigation failed: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
