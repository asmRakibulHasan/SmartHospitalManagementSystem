package com.example.smarthospitalmanagementsystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthospitalmanagementsystem.databinding.ItemDoctorBinding

data class Doctor(
    val imageRes: Int,
    val name: String,
    val specialist: String,
    val rating: Float
)

class DoctorAdapter(
    private val doctorList: List<Doctor>,
    private val onBookNowClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(val binding: ItemDoctorBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        with(holder.binding) {
            imgDoctor.setImageResource(doctor.imageRes)
            tvName.text = doctor.name
            tvSpecialist.text = doctor.specialist
            tvRating.text = "${doctor.rating} Rating"
            btnBook.setOnClickListener { onBookNowClick(doctor) }
        }
    }

    override fun getItemCount(): Int = doctorList.size
}
