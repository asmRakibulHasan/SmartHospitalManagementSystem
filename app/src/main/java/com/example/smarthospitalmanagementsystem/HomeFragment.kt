package com.example.smarthospitalmanagementsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.smarthospitalmanagementsystem.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var btnLogin: Button
    private lateinit var btnRegistration: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        btnLogin = view.findViewById(R.id.btn_login)
        btnRegistration = view.findViewById(R.id.btn_registration)

        // Set click listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Navigate to Login Fragment
        btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        // Navigate to Registration Fragment
        btnRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registraionFragment)
        }
    }
}
