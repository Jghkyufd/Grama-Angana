package com.example.grama_angana.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grama_angana.data.local.MaintenanceItem
import com.example.grama_angana.databinding.FragmentMaintenanceBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaintenanceFragment : Fragment() {

    private var _binding: FragmentMaintenanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MaintenanceViewModel by viewModels()
    private lateinit var adapter: MaintenanceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMaintenanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MaintenanceAdapter { item ->
            viewModel.pledgeAmount(item, 50)
        }
        binding.rvMaintenance.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMaintenance.adapter = adapter

        viewModel.allItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        binding.fabAddMaintenance.setOnClickListener {
            showAddItemDialog()
        }

        checkFirstLaunch()
    }

    private fun checkFirstLaunch() {
        val prefs = requireContext().getSharedPreferences("grama_angana_prefs", Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("is_first_launch", true)
        if (isFirstLaunch) {
            val sampleItems = listOf(
                MaintenanceItem(itemName = "New Ceiling Fan", description = "Buy a new fan for the main hall.", estimatedCost = 1200, amountPledged = 400),
                MaintenanceItem(itemName = "Replace Broken Chairs (x4)", description = "Need 4 new plastic chairs.", estimatedCost = 800, amountPledged = 200),
                MaintenanceItem(itemName = "Repaint Entrance Wall", description = "Painting the main entrance.", estimatedCost = 500, amountPledged = 500, isCompleted = true)
            )
            sampleItems.forEach { viewModel.insertItem(it) }
            prefs.edit().putBoolean("is_first_launch", false).apply()
        }
    }

    private fun showAddItemDialog() {
        val context = requireContext()
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val etName = EditText(context).apply { hint = "Item Name" }
        val etDesc = EditText(context).apply { hint = "Description" }
        val etCost = EditText(context).apply { 
            hint = "Estimated Cost"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(etName)
        layout.addView(etDesc)
        layout.addView(etCost)

        MaterialAlertDialogBuilder(context)
            .setTitle("Add Maintenance Item")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val costStr = etCost.text.toString().trim()
                if (name.isNotEmpty() && desc.isNotEmpty() && costStr.isNotEmpty()) {
                    val cost = costStr.toIntOrNull() ?: 0
                    viewModel.insertItem(MaintenanceItem(itemName = name, description = desc, estimatedCost = cost))
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
