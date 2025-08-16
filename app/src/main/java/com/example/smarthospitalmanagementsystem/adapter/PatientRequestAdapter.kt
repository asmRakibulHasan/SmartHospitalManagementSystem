package com.example.smarthospitalmanagementsystem.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthospitalmanagementsystem.databinding.ItemPatientRequestBinding

// Data class for Patient Request
data class PatientRequest(
    val imageRes: Int,
    val patientName: String,
    val schedule: String
)

// RecyclerView Adapter
class PatientRequestAdapter(
    private val patientRequests: List<PatientRequest>,
    private val onAcceptClick: (PatientRequest) -> Unit
) : RecyclerView.Adapter<PatientRequestAdapter.PatientRequestViewHolder>() {

    inner class PatientRequestViewHolder(private val binding: ItemPatientRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(patientRequest: PatientRequest) {
            binding.apply {
                ivPatient.setImageResource(patientRequest.imageRes)
                tvPatientName.text = patientRequest.patientName
                tvSchedule.text = patientRequest.schedule

                btnAccept.setOnClickListener {
                    onAcceptClick(patientRequest)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientRequestViewHolder {
        val binding = ItemPatientRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PatientRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientRequestViewHolder, position: Int) {
        holder.bind(patientRequests[position])
    }

    override fun getItemCount(): Int = patientRequests.size
}
