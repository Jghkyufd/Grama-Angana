package com.example.grama_angana.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grama_angana.data.remote.Booking
import com.example.grama_angana.data.remote.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {
    private val repository = BookingRepository()

    private val _bookings = MutableLiveData<List<Booking>>()
    val bookings: LiveData<List<Booking>> = _bookings

    private val _bookingResult = MutableLiveData<Result<String>>()
    val bookingResult: LiveData<Result<String>> = _bookingResult

    init {
        repository.getAllBookings {
            _bookings.postValue(it)
        }
    }

    fun requestBooking(booking: Booking) {
        viewModelScope.launch {
            val result = repository.requestBooking(booking)
            _bookingResult.postValue(result)
        }
    }
}
