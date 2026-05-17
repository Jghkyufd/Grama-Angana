package com.example.grama_angana.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grama_angana.data.local.Announcement
import com.example.grama_angana.databinding.ItemEventBinding

class EventAdapter : ListAdapter<Announcement, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Announcement) {
            binding.tvTitle.text = event.title
            binding.tvDate.text = event.date
            binding.tvDescription.text = event.description

            if (event.imageUrl.isNotEmpty()) {
                binding.ivEventImage.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(event.imageUrl)
                    .into(binding.ivEventImage)
            } else {
                binding.ivEventImage.visibility = View.GONE
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }
    }
}
