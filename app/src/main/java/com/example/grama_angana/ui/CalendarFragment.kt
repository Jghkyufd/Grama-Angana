package com.example.grama_angana.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grama_angana.R
import com.example.grama_angana.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: BookingViewModel by activityViewModels()
    private lateinit var adapter: BookingAdapter
    private var selectedDateStr: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookingAdapter()
        binding.rvBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookings.adapter = adapter

        // Set initial date
        val calendar = Calendar.getInstance()
        selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        updateHeader()

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)
            selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendar.time)
            updateHeader()
            filterBookings()
        }

        viewModel.bookings.observe(viewLifecycleOwner) {
            filterBookings()
        }

        binding.fabRequestBooking.setOnClickListener {
            // Find NavController and navigate
            findNavController().navigate(R.id.navigation_booking)
        }
    }

    private fun updateHeader() {
        binding.tvDateHeader.text = "Bookings for $selectedDateStr"
    }

    private fun filterBookings() {
        val allBookings = viewModel.bookings.value ?: emptyList()
        val filtered = allBookings.filter { it.date == selectedDateStr }
        adapter.submitList(filtered)
        binding.tvEmptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
