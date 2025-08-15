package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthospitalmanagementsystem.adapter.Doctor
import com.example.smarthospitalmanagementsystem.adapter.DoctorAdapter
import com.example.smarthospitalmanagementsystem.databinding.FragmentLoginBinding
import com.example.smarthospitalmanagementsystem.databinding.FragmentPatientBinding


class PatientFragment : Fragment() {
    private lateinit var doctorAdapter: DoctorAdapter
    private var _binding: FragmentPatientBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val doctors = listOf(
            Doctor(R.drawable.doctor_photo_background, "Dr. Sarah Mitchell", "Ophthalmologist", 4.2f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Marvin King", "Psychiatrist", 4.2f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Emily Carter", "General Practitioner", 3.6f),
            Doctor(R.drawable.doctor_photo_background, "Dr. James Wright", "Dermatologist", 5.0f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Olivia Evans", "Psychiatrist", 4.8f),
            Doctor(R.drawable.doctor_photo_background, "Dr. William Harris", "Neurologist", 3.5f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Mia Thompson", "Oncologist", 4.3f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Benjamin Scott", "Pediatrician", 3.6f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Ava Martinez", "General Practitioner", 4.8f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Lucas Clark", "Dermatologist", 4.1f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Isabella Lewis", "Cardiologist", 3.6f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Henry Walker", "Ophthalmologist", 4.6f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Charlotte Young", "Psychiatrist", 4.8f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Alexander Hill", "Dermatologist", 4.5f),
            Doctor(R.drawable.doctor_photo_background, "Dr. Amelia Green", "Neurologist", 4.1f)
        )

        doctorAdapter = DoctorAdapter(doctors) { doctor ->
            findNavController().navigate(R.id.action_patientFragment_to_prescriptionFragment)
        }
        binding.recyclerDoctors.adapter = doctorAdapter
        binding.recyclerDoctors.layoutManager = LinearLayoutManager(requireContext())
    }

}