package com.example.grama_angana.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.grama_angana.databinding.FragmentBookingFormBinding
import com.example.grama_angana.data.remote.Booking
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingFormFragment : Fragment() {

    private var _binding: FragmentBookingFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookingFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeSlots = arrayOf("Morning (08:00 - 12:00)", "Afternoon (12:00 - 16:00)", "Evening (16:00 - 20:00)", "Full Day")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, timeSlots)
        binding.spinnerTimeSlot.adapter = adapter

        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selected = Calendar.getInstance()
                    selected.set(year, month, dayOfMonth)
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    binding.etDate.setText(format.format(selected.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        binding.btnSubmit.setOnClickListener {
            submitForm()
        }

        viewModel.bookingResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { message ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Success")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    .show()
            }.onFailure { exception ->
                Snackbar.make(binding.root, exception.message ?: "An error occurred", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun submitForm() {
        val fullName = binding.etFullName.text.toString().trim()
        val purpose = binding.etPurpose.text.toString().trim()
        val contact = binding.etContact.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val timeSlot = binding.spinnerTimeSlot.selectedItem.toString()

        if (fullName.isEmpty() || purpose.isEmpty() || contact.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val booking = Booking(
            title = purpose,
            bookedBy = fullName,
            date = date,
            timeSlot = timeSlot,
            purpose = purpose,
            contactNumber = contact,
            status = "pending"
        )

        viewModel.requestBooking(booking)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
