package com.demo.foodorderanddeliveryappkotlin.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.MenuListAdapter
import com.demo.foodorderanddeliveryappkotlin.models.Menus
import com.demo.foodorderanddeliveryappkotlin.models.RestaurantModel
import com.google.android.material.button.MaterialButton

class RestaurantMenuActivity : BaseActivity(), MenuListAdapter.MenuListClickListener {
    private lateinit var restaurantModel: RestaurantModel
    private lateinit var menuListAdapter: MenuListAdapter
    private lateinit var menuRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RestaurantMenuActivity", "onCreate called")
        
        setContentView(R.layout.activity_restaurant_menu)
        
        try {
            // Get restaurant model from intent with proper Android Tiramisu support
            restaurantModel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("RestaurantModel", RestaurantModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("RestaurantModel")
            } ?: throw IllegalArgumentException("RestaurantModel is null")
                
            Log.d("RestaurantMenuActivity", "Successfully loaded restaurant: ${restaurantModel.name}")
        } catch (e: Exception) {
            Log.e("RestaurantMenuActivity", "Error loading restaurant model", e)
            Toast.makeText(this, "Error loading restaurant details: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRestaurantInfo()
        setupRecyclerView()
        setupPlaceOrderButton()
    }

    override fun onStart() {
        super.onStart()
        setSelectedItem(R.id.nav_home)
    }

    private fun setupRestaurantInfo() {
        try {
            val restaurantName = findViewById<TextView>(R.id.restaurantName)
                ?: throw NullPointerException("Could not find restaurantName TextView")
                
            val restaurantImage = findViewById<ImageView>(R.id.restaurantImage)
                ?: throw NullPointerException("Could not find restaurantImage ImageView")
                
            val restaurantAddress = findViewById<TextView>(R.id.restaurantAddress)
                ?: throw NullPointerException("Could not find restaurantAddress TextView")
                
            val restaurantRating = findViewById<TextView>(R.id.restaurantRating)
                ?: throw NullPointerException("Could not find restaurantRating TextView")

            restaurantName.text = restaurantModel.name ?: "Unknown Restaurant"
            restaurantAddress.text = restaurantModel.address ?: "Address unavailable"
            restaurantRating.text = "Rating: ${restaurantModel.rating}"

            Glide.with(this)
                .load(restaurantModel.image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(restaurantImage)
            
        } catch (e: Exception) {
            Log.e("RestaurantMenuActivity", "Error setting up restaurant info", e)
            Toast.makeText(this, "Error displaying restaurant details: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        menuRecyclerView = findViewById(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(this)
        menuListAdapter = MenuListAdapter(
            menuList = restaurantModel.menus,
            restaurantId = restaurantModel.id ?: "",
            restaurantName = restaurantModel.name ?: "",
            clickListener = this
        )
        menuRecyclerView.adapter = menuListAdapter
    }

    private fun setupPlaceOrderButton() {
        val checkoutButton = findViewById<MaterialButton>(R.id.checkoutButton)
        checkoutButton.setOnClickListener {
            Log.d("RestaurantMenuActivity", "Checkout button clicked, navigating to CartActivity")
            try {
                val itemsInCart = restaurantModel.menus?.filterNotNull()?.any { menu -> menu.totalInCart > 0 } ?: false
                
                if (itemsInCart) {
                    navigateTo(CartActivity::class.java) { intent ->
                        intent.putExtra("RestaurantModel", restaurantModel)
                    }
                } else {
                    Toast.makeText(this, "Please add items to your cart first", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RestaurantMenuActivity", "Error navigating to CartActivity", e)
                Toast.makeText(this, "Unable to proceed to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun addToCartClickListener(menu: Menus) {
        updateMenuInModel(menu)
    }

    override fun updateCartClickListener(menu: Menus) {
        updateMenuInModel(menu)
    }

    override fun removeFromCartClickListener(menu: Menus) {
        updateMenuInModel(menu)
    }
    
    private fun updateMenuInModel(menu: Menus) {
        val index = restaurantModel.menus?.indexOfFirst { item -> item?.name == menu.name } ?: -1
        if (index != -1) {
            val updatedMenus = restaurantModel.menus?.toMutableList() ?: mutableListOf()
            updatedMenus[index] = menu
            restaurantModel.menus = updatedMenus
            menuListAdapter.notifyItemChanged(index)
        }
    }
} 