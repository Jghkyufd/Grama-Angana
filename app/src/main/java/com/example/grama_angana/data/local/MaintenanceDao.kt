package com.example.grama_angana.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_items ORDER BY isCompleted ASC, id DESC")
    fun getAllItems(): LiveData<List<MaintenanceItem>>

    @Insert
    suspend fun insertItem(item: MaintenanceItem)

    @Query("UPDATE maintenance_items SET amountPledged = :newAmount WHERE id = :id")
    suspend fun updatePledge(id: Int, newAmount: Int)

    @Query("UPDATE maintenance_items SET isCompleted = 1 WHERE id = :id")
    suspend fun markCompleted(id: Int)
}
