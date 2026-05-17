package com.example.grama_angana

import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.grama_angana.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        val booking = hashMapOf(
            "name" to "Ravi",
            "event" to "Wedding",
            "date" to "25-05-2026",
            "status" to "Pending"
        )

        db.collection("booking")
            .add(booking)
            .addOnSuccessListener {
                println("Booking Added")
            }
            .addOnFailureListener {
                println("Error")
            }
        val maintenance = hashMapOf(
            "item" to "Fan Repair",
            "amountNeeded" to "500",
            "status" to "Open"
        )
        db.collection("maintenance")
            .add(maintenance)
        val event = hashMapOf(
            "eventName" to "Health Camp",
            "date" to "30-05-2026",
            "location" to "Village Hall"
        )

        db.collection("events")
            .add(event)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_calendar,
                R.id.navigation_booking,
                R.id.navigation_maintenance,
                R.id.navigation_events
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnFailureListener {
                    android.widget.Toast.makeText(this, "Firebase Auth failed: ${it.message}", android.widget.Toast.LENGTH_LONG).show()
                }
        }
    }
}
