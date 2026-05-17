package com.example.grama_angana.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.grama_angana.data.local.Announcement
import com.example.grama_angana.data.local.AppDatabase
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).eventDao()

    val events: LiveData<List<Announcement>> = dao.getAllEvents()

    fun addEvent(event: Announcement) {
        viewModelScope.launch {
            dao.insertEvent(event)
        }
    }
}
