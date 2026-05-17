package com.example.grama_angana.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grama_angana.data.local.Announcement
import com.example.grama_angana.databinding.FragmentEventBoardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EventBoardFragment : Fragment() {

    private var _binding: FragmentEventBoardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EventViewModel by activityViewModels()
    private lateinit var adapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventAdapter()
        binding.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvents.adapter = adapter

        val prefs = requireContext().getSharedPreferences("grama_angana_prefs", Context.MODE_PRIVATE)
        val isAdmin = prefs.getBoolean("is_admin", true) // Default true for testing
        
        if (isAdmin) {
            binding.fabAddEvent.visibility = View.VISIBLE
            binding.fabAddEvent.setOnClickListener {
                showAddEventDialog()
            }
        } else {
            binding.fabAddEvent.visibility = View.GONE
        }

        viewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            if (events.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.tvEmptyState.visibility = View.GONE
            }
        }

        checkFirstLaunchAndAddSamples(prefs)
    }

    private fun checkFirstLaunchAndAddSamples(prefs: android.content.SharedPreferences) {
        val isFirstLaunchEvents = prefs.getBoolean("is_first_launch_events", true)
        if (isFirstLaunchEvents) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            viewModel.addEvent(
                Announcement(
                    title = "Village Meeting (Grama Sabha)",
                    description = "Monthly meeting to discuss local development projects.",
                    date = today,
                    imageUrl = ""
                )
            )
            viewModel.addEvent(
                Announcement(
                    title = "Health Camp",
                    description = "Free eye checkup and general health screening at the Panchayat office.",
                    date = today,
                    imageUrl = ""
                )
            )
            prefs.edit().putBoolean("is_first_launch_events", false).apply()
        }
    }

    private fun showAddEventDialog() {
        val context = requireContext()
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val etTitle = EditText(context).apply { hint = "Event Title" }
        val etDesc = EditText(context).apply { hint = "Description" }

        layout.addView(etTitle)
        layout.addView(etDesc)

        MaterialAlertDialogBuilder(context)
            .setTitle("Add Event")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val title = etTitle.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                if (title.isNotEmpty() && desc.isNotEmpty()) {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
                    viewModel.addEvent(Announcement(title = title, description = desc, date = date))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
