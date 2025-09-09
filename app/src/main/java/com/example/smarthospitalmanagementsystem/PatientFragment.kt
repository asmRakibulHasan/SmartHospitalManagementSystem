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
        )

        doctorAdapter = DoctorAdapter(doctors) { doctor ->
            findNavController().navigate(R.id.action_patientFragment_to_doctorProfileFragment)
        }
        binding.recyclerDoctors.adapter = doctorAdapter
        binding.recyclerDoctors.layoutManager = LinearLayoutManager(requireContext())
    }

}