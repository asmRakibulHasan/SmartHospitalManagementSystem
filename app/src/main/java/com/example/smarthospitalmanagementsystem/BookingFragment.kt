package com.example.smarthospitalmanagementsystem

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smarthospitalmanagementsystem.databinding.FragmentBookingBinding
import com.example.smarthospitalmanagementsystem.viewmodel.DoctorPatientViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BookingFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel
    private val viewModel: DoctorPatientViewModel by activityViewModels()

    // Demo doctor data
    private val doctorName = "Dr. Sarah Mitchell"
    private val doctorSpecialty = "Ophthalmologist"
    private val chamberLocation = "City Medical Center\n123 Medical Street, Dhaka"

    // Selected appointment details
    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupDatePicker()
        setupTimeSpinner()
        setupClickListeners()
        observeAppointmentBooking()
    }

    private fun setupUI() {
        // Set demo doctor details
        binding.tvDoctorName.text = doctorName
        binding.tvDoctorSpecialty.text = doctorSpecialty
        binding.tvChamberLocation.text = chamberLocation
    }

    private fun setupDatePicker() {
        binding.etDatePicker.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectedCalendar.time)

                binding.etDatePicker.setText(selectedDate)
                Log.d("BookingFragment", "Selected date: $selectedDate")
            },
            year, month, day
        )

        // Set minimum date to today
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun setupTimeSpinner() {
        // Available time slots
        val timeSlots = arrayOf(
            "Select time",
            "09:00 AM",
            "10:00 AM",
            "11:00 AM",
            "02:00 PM",
            "03:00 PM"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            timeSlots
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTime.adapter = adapter

        // Set selection listener
        binding.spinnerTime.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    selectedTime = timeSlots[position]
                    Log.d("BookingFragment", "Selected time: $selectedTime")
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupClickListeners() {
        binding.btnPayNow.setOnClickListener {
            validateAndBookAppointment()
        }
    }

    private fun validateAndBookAppointment() {
        when {
            selectedDate.isEmpty() -> {
                Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
                return
            }
            selectedTime.isEmpty() || selectedTime == "Select time" -> {
                Toast.makeText(requireContext(), "Please select a time", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                bookAppointment()
            }
        }
    }

    private fun bookAppointment() {
        Log.d("BookingFragment", "=== Booking Appointment ===")
        Log.d("BookingFragment", "Doctor: $doctorName")
        Log.d("BookingFragment", "Date: $selectedDate")
        Log.d("BookingFragment", "Time: $selectedTime")
        Log.d("BookingFragment", "==========================")

        // Show loading state
        setPayButtonLoading(true)

        // Book appointment through ViewModel
        viewModel.bookAppointment(
            doctorName = doctorName,
            doctorSpecialty = doctorSpecialty,
            appointmentDate = selectedDate,
            appointmentTime = selectedTime,
            chamberLocation = chamberLocation
        )
    }

    private fun observeAppointmentBooking() {
        lifecycleScope.launch {
            viewModel.appointmentBookingState.collect { success ->
                when (success) {
                    true -> {
                        setPayButtonLoading(false)

                        Toast.makeText(
                            requireContext(),
                            "Appointment booked successfully!",
                            Toast.LENGTH_LONG
                        ).show()

                        viewModel.lastSelectedDate = selectedDate
                        viewModel.lastSelectedTime = selectedTime

                        // Navigate to payment fragment
                        findNavController().navigate(R.id.action_bookingFragment_to_paymentFragment)

                        // Reset state
                        viewModel.resetAppointmentBookingState()
                    }
                    false -> {
                        setPayButtonLoading(false)

                        Toast.makeText(
                            requireContext(),
                            "Failed to book appointment. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()

                        // Reset state
                        viewModel.resetAppointmentBookingState()
                    }
                    null -> {
                        // Initial state, do nothing
                    }
                }
            }
        }
    }

    private fun setPayButtonLoading(isLoading: Boolean) {
        binding.btnPayNow.apply {
            isEnabled = !isLoading
            text = if (isLoading) "Processing..." else "Pay Now"
            alpha = if (isLoading) 0.7f else 1.0f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
