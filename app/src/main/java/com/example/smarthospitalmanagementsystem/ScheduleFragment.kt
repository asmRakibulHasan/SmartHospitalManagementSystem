package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthospitalmanagementsystem.adapter.Schedule
import com.example.smarthospitalmanagementsystem.adapter.ScheduleAdapter
import com.example.smarthospitalmanagementsystem.databinding.FragmentScheduleBinding


class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val schedules = listOf(
            Schedule(R.drawable.doctor_photo_background, "Mrs. Sarah Mac", "Today at 9 am"),
            Schedule(R.drawable.doctor_photo_background, "Devon Lane", "Today at 10 am"),
            Schedule(R.drawable.doctor_photo_background, "Savannah Nguyen", "Today at 11 am"),
            Schedule(R.drawable.doctor_photo_background, "Robert Fox", "Today at 12 pm"),
            Schedule(R.drawable.doctor_photo_background, "Jane Cooper", "Today at 4 pm"),
            Schedule(R.drawable.doctor_photo_background, "Kristin Watson", "Today at 5 pm"),
            Schedule(R.drawable.doctor_photo_background, "Devon Lane", "Today at 10 am"),
            Schedule(R.drawable.doctor_photo_background, "Savannah Nguyen", "Today at 11 am"),
            Schedule(R.drawable.doctor_photo_background, "Robert Fox", "Today at 12 pm"),
            Schedule(R.drawable.doctor_photo_background, "Jane Cooper", "Today at 4 pm"),
            Schedule(R.drawable.doctor_photo_background, "Kristin Watson", "Today at 5 pm"),
            Schedule(R.drawable.doctor_photo_background, "Devon Lane", "Today at 10 am"),
            Schedule(R.drawable.doctor_photo_background, "Savannah Nguyen", "Today at 11 am"),
            Schedule(R.drawable.doctor_photo_background, "Robert Fox", "Today at 12 pm"),
            Schedule(R.drawable.doctor_photo_background, "Jane Cooper", "Today at 4 pm"),
            Schedule(R.drawable.doctor_photo_background, "Kristin Watson", "Today at 5 pm"),
            Schedule(R.drawable.doctor_photo_background, "Devon Lane", "Today at 10 am"),
            Schedule(R.drawable.doctor_photo_background, "Savannah Nguyen", "Today at 11 am"),
            Schedule(R.drawable.doctor_photo_background, "Robert Fox", "Today at 12 pm"),
            Schedule(R.drawable.doctor_photo_background, "Jane Cooper", "Today at 4 pm"),
            Schedule(R.drawable.doctor_photo_background, "Kristin Watson", "Today at 5 pm")
        )

        scheduleAdapter = ScheduleAdapter(schedules)
        binding.recyclerSchedule.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
