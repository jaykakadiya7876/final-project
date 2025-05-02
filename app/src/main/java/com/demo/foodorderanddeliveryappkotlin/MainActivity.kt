package com.demo.foodorderanddeliveryappkotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demo.foodorderanddeliveryappkotlin.activities.RestaurantListActivity

/**
 * MainActivity now serves as a redirector to the RestaurantListActivity
 * to avoid duplicate functionality.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log for debugging
        Log.d("MainActivity", "Redirecting to RestaurantListActivity")
        
        // Redirect to RestaurantListActivity
        val intent = Intent(this, RestaurantListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear any existing instances
        startActivity(intent)
        
        Log.d("MainActivity", "Finishing MainActivity")
        finish() // Close this activity so it doesn't stay in the back stack
    }
}