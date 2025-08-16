package com.example.smarthospitalmanagementsystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthospitalmanagementsystem.databinding.ItemScheduleBinding

data class Schedule(
    val imageRes: Int,
    val name: String,
    val time: String
)

class ScheduleAdapter(
    private val schedules: List<Schedule>
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(private val binding: ItemScheduleBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {
            binding.imgUser.setImageResource(schedule.imageRes)
            binding.tvName.text = schedule.name
            binding.tvScheduleTime.text = schedule.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(schedules[position])
    }

    override fun getItemCount(): Int = schedules.size
}
