package com.example.grama_angana.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance_items")
data class MaintenanceItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemName: String,
    val description: String,
    val estimatedCost: Int,
    val amountPledged: Int = 0,
    val isCompleted: Boolean = false
)
