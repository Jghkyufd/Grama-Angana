package com.example.grama_angana.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MaintenanceItem::class, Announcement::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "GramaAnganaDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
