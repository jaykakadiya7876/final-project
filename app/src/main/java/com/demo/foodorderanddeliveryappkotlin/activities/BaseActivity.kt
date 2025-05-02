package com.demo.foodorderanddeliveryappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demo.foodorderanddeliveryappkotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setupBottomNavigation will be called in onStart
    }
    
    override fun onStart() {
        super.onStart()
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        try {
            bottomNavigation = findViewById(R.id.bottomNavigation) ?: return
            
            bottomNavigation.setOnItemSelectedListener { item ->
                // Don't navigate if we're already on this screen
                if (item.itemId == bottomNavigation.selectedItemId) {
                    return@setOnItemSelectedListener true
                }
                
                when (item.itemId) {
                    R.id.nav_home -> {
                        if (this !is RestaurantListActivity) {
                            navigateTo(RestaurantListActivity::class.java)
                        }
                        true
                    }
                    R.id.nav_orders -> {
                        if (this !is MyOrdersActivity) {
                            navigateTo(MyOrdersActivity::class.java)
                        }
                        true
                    }
                    R.id.nav_cart -> {
                        if (this !is CartActivity) {
                            navigateTo(CartActivity::class.java)
                        }
                        true
                    }
                    R.id.nav_favorites -> {
                        if (this !is FavoritesActivity) {
                            navigateTo(FavoritesActivity::class.java)
                        }
                        true
                    }
                    R.id.nav_profile -> {
                        if (this !is MainActivity) {
                            navigateTo(MainActivity::class.java)
                        }
                        true
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            Log.e("BaseActivity", "Error setting up bottom navigation", e)
        }
    }
    
    // Consistent navigation method to use across the app
    protected fun navigateTo(destinationClass: Class<*>, extras: ((Intent) -> Unit)? = null) {
        try {
            val intent = Intent(this, destinationClass)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            extras?.invoke(intent)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("BaseActivity", "Error navigating to ${destinationClass.simpleName}", e)
        }
    }

    protected fun setSelectedItem(itemId: Int) {
        try {
            if (::bottomNavigation.isInitialized) {
                bottomNavigation.selectedItemId = itemId
            }
        } catch (e: Exception) {
            Log.e("BaseActivity", "Error setting selected navigation item", e)
        }
    }
}