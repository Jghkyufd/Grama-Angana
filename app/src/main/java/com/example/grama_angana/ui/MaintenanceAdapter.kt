package com.example.grama_angana.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grama_angana.data.local.MaintenanceItem
import com.example.grama_angana.databinding.ItemMaintenanceBinding

class MaintenanceAdapter(
    private val onPledgeClick: (MaintenanceItem) -> Unit
) : ListAdapter<MaintenanceItem, MaintenanceAdapter.MaintenanceViewHolder>(MaintenanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = ItemMaintenanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaintenanceViewHolder(binding, onPledgeClick)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MaintenanceViewHolder(
        private val binding: ItemMaintenanceBinding,
        private val onPledgeClick: (MaintenanceItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: MaintenanceItem) {
            binding.tvItemName.text = item.itemName
            binding.tvDescription.text = item.description
            binding.tvCost.text = "Estimated Cost: ₹${item.estimatedCost}"
            binding.tvPledged.text = "Pledged: ₹${item.amountPledged}"

            val progress = if (item.estimatedCost > 0) {
                (item.amountPledged * 100) / item.estimatedCost
            } else 0
            binding.progressBar.progress = progress

            if (item.isCompleted) {
                binding.tvBadge.visibility = View.VISIBLE
                binding.btnPledge.visibility = View.GONE
            } else {
                binding.tvBadge.visibility = View.GONE
                binding.btnPledge.visibility = View.VISIBLE
            }

            binding.btnPledge.setOnClickListener {
                onPledgeClick(item)
            }
        }
    }

    class MaintenanceDiffCallback : DiffUtil.ItemCallback<MaintenanceItem>() {
        override fun areItemsTheSame(oldItem: MaintenanceItem, newItem: MaintenanceItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MaintenanceItem, newItem: MaintenanceItem): Boolean {
            return oldItem == newItem
        }
    }
}
