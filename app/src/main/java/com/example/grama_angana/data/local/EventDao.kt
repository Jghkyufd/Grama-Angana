package com.example.grama_angana.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM announcements ORDER BY date DESC, id DESC")
    fun getAllEvents(): LiveData<List<Announcement>>

    @Insert
    suspend fun insertEvent(event: Announcement)
}
