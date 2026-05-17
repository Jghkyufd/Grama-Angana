package com.example.grama_angana.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val imageUrl: String = ""
)
