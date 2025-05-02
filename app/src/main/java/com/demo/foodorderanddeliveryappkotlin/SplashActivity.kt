package com.demo.foodorderanddeliveryappkotlin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.demo.foodorderanddeliveryappkotlin.activities.BaseActivity
import com.demo.foodorderanddeliveryappkotlin.activities.RestaurantListActivity

class SplashActivity : BaseActivity() {
    private lateinit var mAuth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Use Handler with Looper.getMainLooper() for newer Android versions
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if user is signed in
            val currentUser = mAuth.currentUser
            if (currentUser != null) {
                // User is already logged in, go to RestaurantListActivity directly
                navigateTo(RestaurantListActivity::class.java)
            } else {
                // User is not logged in, go to AuthActivity
                navigateTo(AuthActivity::class.java)
            }
            finish()
        }, 2000)
    }
}