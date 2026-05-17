package com.example.grama_angana.data.remote

data class Booking(
    val id: String = "",
    val title: String = "",
    val bookedBy: String = "",
    val date: String = "",
    val timeSlot: String = "",
    val status: String = "pending", // pending, approved, rejected
    val purpose: String = "",
    val contactNumber: String = ""
)
