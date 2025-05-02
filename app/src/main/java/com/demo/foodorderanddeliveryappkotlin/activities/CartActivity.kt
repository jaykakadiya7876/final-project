package com.demo.foodorderanddeliveryappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.CartAdapter
import com.demo.foodorderanddeliveryappkotlin.models.Menus
import com.demo.foodorderanddeliveryappkotlin.models.RestaurantModel

class CartActivity : BaseActivity() {
    private lateinit var restaurantModel: RestaurantModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartItemsRecyclerView: RecyclerView
    private lateinit var tvSubtotalAmount: TextView
    private lateinit var tvDeliveryChargeAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnCheckout: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSelectedItem(R.id.nav_cart)
        
        try {
            restaurantModel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("RestaurantModel", RestaurantModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("RestaurantModel")
            } ?: throw IllegalArgumentException("RestaurantModel must not be null")
            
            Log.d("CartActivity", "Successfully loaded restaurant model: ${restaurantModel.name}")
            
            setupViews()
            setupRecyclerView()
            setupOrderSummary()
            setupCheckoutButton()
        } catch (e: Exception) {
            Log.e("CartActivity", "Failed to initialize CartActivity", e)
            Toast.makeText(this, "Error loading cart details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupViews() {
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView)
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount)
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        btnCheckout = findViewById(R.id.btnCheckout)
    }
    
    private fun setupRecyclerView() {
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        val cartItems = restaurantModel.menus?.filterNotNull()?.filter { menu -> menu.totalInCart > 0 } ?: emptyList()
        cartAdapter = CartAdapter(
            cartItems,
            onItemRemoved = { menu ->
                menu.totalInCart = 0
                updateOrderSummary()
                val updatedItems = restaurantModel.menus?.filterNotNull()?.filter { item -> item.totalInCart > 0 } ?: emptyList()
                cartAdapter.updateItems(updatedItems)
            }
        )
        cartItemsRecyclerView.adapter = cartAdapter
    }

    private fun setupOrderSummary() {
        updateOrderSummary()
    }
    
    private fun updateOrderSummary() {
        val subtotal = restaurantModel.menus?.filterNotNull()?.sumOf { menu ->
            (menu.price * menu.totalInCart).toDouble()
        } ?: 0.0
        val deliveryCharge = 5.0 // Fixed delivery charge
        val total = subtotal + deliveryCharge

        tvSubtotalAmount.text = String.format("$%.2f", subtotal)
        tvDeliveryChargeAmount.text = String.format("$%.2f", deliveryCharge)
        tvTotalAmount.text = String.format("$%.2f", total)
    }
    
    private fun setupCheckoutButton() {
        btnCheckout.setOnClickListener {
            try {
                val cartItems = restaurantModel.menus?.filterNotNull()?.filter { menu -> menu.totalInCart > 0 }
                
                if (!cartItems.isNullOrEmpty()) {
                    Log.d("CartActivity", "Starting PlaceYourOrderActivity with ${cartItems.size} items")
                    navigateTo(PlaceYourOrderActivity::class.java) { intent ->
                        intent.putExtra("RestaurantModel", restaurantModel)
                    }
                } else {
                    Log.d("CartActivity", "No items in cart")
                    Toast.makeText(this, "Please add items to your cart first", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("CartActivity", "Error starting PlaceYourOrderActivity", e)
                Toast.makeText(this, "Error proceeding to checkout", Toast.LENGTH_SHORT).show()
            }
        }
    }
}