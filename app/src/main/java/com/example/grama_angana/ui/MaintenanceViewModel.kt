package com.example.grama_angana.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.grama_angana.data.local.AppDatabase
import com.example.grama_angana.data.local.MaintenanceItem
import kotlinx.coroutines.launch

class MaintenanceViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).maintenanceDao()
    val allItems: LiveData<List<MaintenanceItem>> = dao.getAllItems()

    fun insertItem(item: MaintenanceItem) {
        viewModelScope.launch {
            dao.insertItem(item)
        }
    }

    fun pledgeAmount(item: MaintenanceItem, amount: Int) {
        viewModelScope.launch {
            val newAmount = item.amountPledged + amount
            dao.updatePledge(item.id, newAmount)
            if (newAmount >= item.estimatedCost) {
                dao.markCompleted(item.id)
            }
        }
    }
}
