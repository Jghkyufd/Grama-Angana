package com.example.grama_angana.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grama_angana.data.remote.Booking
import com.example.grama_angana.databinding.ItemBookingBinding

class BookingAdapter : ListAdapter<Booking, BookingAdapter.BookingViewHolder>(BookingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookingViewHolder(private val binding: ItemBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.tvTitle.text = booking.title
            binding.tvBookedBy.text = "By: ${booking.bookedBy}"
            binding.tvTimeSlot.text = "Time: ${booking.timeSlot}"
            binding.tvStatus.text = booking.status.uppercase()
            
            when (booking.status) {
                "approved" -> binding.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"))
                "rejected" -> binding.tvStatus.setBackgroundColor(Color.parseColor("#F44336"))
                else -> binding.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"))
            }
        }
    }

    class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }
}
