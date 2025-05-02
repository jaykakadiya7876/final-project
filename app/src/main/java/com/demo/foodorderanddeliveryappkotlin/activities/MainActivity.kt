package com.demo.foodorderanddeliveryappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.demo.foodorderanddeliveryappkotlin.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        setSelectedItem(R.id.nav_profile)

        val btnRestaurants = findViewById<Button>(R.id.btnRestaurants)
        val btnMyOrders = findViewById<Button>(R.id.btnMyOrders)
        val btnSignOut = findViewById<Button>(R.id.btnSignOut)

        btnRestaurants.setOnClickListener {
            val intent = Intent(this, RestaurantListActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnMyOrders.setOnClickListener {
            val intent = Intent(this, MyOrdersActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
} 