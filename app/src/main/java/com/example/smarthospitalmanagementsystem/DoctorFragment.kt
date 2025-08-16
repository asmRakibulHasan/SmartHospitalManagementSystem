package com.example.smarthospitalmanagementsystem

import com.example.smarthospitalmanagementsystem.adapter.PatientRequest
import com.example.smarthospitalmanagementsystem.adapter.PatientRequestAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthospitalmanagementsystem.databinding.FragmentDoctorBinding

class DoctorFragment : Fragment() {

    private var _binding: FragmentDoctorBinding? = null
    private val binding get() = _binding!!

    private lateinit var patientRequestAdapter: PatientRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // Sample data for patient requests
        val patientRequests = listOf(
            PatientRequest(R.drawable.doctor_photo_background, "Mrs. Sarah Mac", "Sunday, evening"),
            PatientRequest(R.drawable.doctor_photo_background, "Ronald Richards", "Sunday, evening"),
            PatientRequest(R.drawable.doctor_photo_background, "Jenny Wilson", "Monday, morning"),
            PatientRequest(R.drawable.doctor_photo_background, "Kristin Watson", "Monday, morning"),
            PatientRequest(R.drawable.doctor_photo_background, "Wade Warren", "Tuesday, afternoon"),
            PatientRequest(R.drawable.doctor_photo_background, "Devon Lane", "Tuesday, evening"),
            PatientRequest(R.drawable.doctor_photo_background, "Wade Warren", "Tuesday, afternoon"),
            PatientRequest(R.drawable.doctor_photo_background, "Devon Lane", "Tuesday, evening"),
            PatientRequest(R.drawable.doctor_photo_background, "Wade Warren", "Tuesday, afternoon"),
            PatientRequest(R.drawable.doctor_photo_background, "Devon Lane", "Tuesday, evening"),
            PatientRequest(R.drawable.doctor_photo_background, "Wade Warren", "Tuesday, afternoon"),
            PatientRequest(R.drawable.doctor_photo_background, "Devon Lane", "Tuesday, evening"),
            PatientRequest(R.drawable.doctor_photo_background, "Wade Warren", "Tuesday, afternoon"),
            PatientRequest(R.drawable.doctor_photo_background, "Devon Lane", "Tuesday, evening")
        )

        // Initialize adapter with click listener
        patientRequestAdapter = PatientRequestAdapter(patientRequests) { patientRequest ->
            handleAcceptClick(patientRequest)
        }

        // Setup RecyclerView
        binding.recyclerPatients.apply {
            adapter = patientRequestAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleAcceptClick(patientRequest: PatientRequest) {
        Log.d("DoctorFragment", "Accept clicked for: ${patientRequest.patientName}")
        Log.d("DoctorFragment", "Schedule: ${patientRequest.schedule}")

        // Show confirmation toast
        Toast.makeText(
            requireContext(),
            "Accepted appointment with ${patientRequest.patientName}",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
