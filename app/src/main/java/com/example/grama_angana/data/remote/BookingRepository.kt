package com.example.grama_angana.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class BookingRepository {
    private val database = FirebaseDatabase.getInstance().reference.child("bookings")

    fun getAllBookings(onDataChange: (List<Booking>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookings = mutableListOf<Booking>()
                for (child in snapshot.children) {
                    val booking = child.getValue(Booking::class.java)
                    if (booking != null) {
                        bookings.add(booking.copy(id = child.key ?: ""))
                    }
                }
                onDataChange(bookings)
            }

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    suspend fun requestBooking(booking: Booking): Result<String> {
        return try {
            // Check for double booking
            val snapshot = database.orderByChild("date").equalTo(booking.date).get().await()
            var isBooked = false
            for (child in snapshot.children) {
                val existingBooking = child.getValue(Booking::class.java)
                if (existingBooking != null && 
                    existingBooking.timeSlot == booking.timeSlot && 
                    existingBooking.status == "approved") {
                    isBooked = true
                    break
                }
            }

            if (isBooked) {
                Result.failure(Exception("Slot already booked. Please select a different time."))
            } else {
                val newRef = database.push()
                val newBooking = booking.copy(id = newRef.key ?: "")
                newRef.setValue(newBooking).await()
                Result.success("Your request has been sent to the Panchayat for approval.")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
