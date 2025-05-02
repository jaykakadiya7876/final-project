package com.demo.foodorderanddeliveryappkotlin.activities

import android.os.Bundle
import android.widget.TextView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.RestaurantModel

class SuccessOrderActivity : BaseActivity() {
    
    private lateinit var buttonDone: TextView
    private lateinit var tvRestaurantName: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)
        
        // Get restaurant model with proper Android Tiramisu support
        val restaurantModel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("RestaurantModel", RestaurantModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<RestaurantModel>("RestaurantModel")
        }
        
        buttonDone = findViewById(R.id.buttonDone)
        tvRestaurantName = findViewById(R.id.tvRestaurantName)
        
        tvRestaurantName.text = "Your order has been successfully placed at " + restaurantModel?.name
        
        buttonDone.setOnClickListener {
            navigateTo(RestaurantListActivity::class.java)
            finish()
        }
    }
} 