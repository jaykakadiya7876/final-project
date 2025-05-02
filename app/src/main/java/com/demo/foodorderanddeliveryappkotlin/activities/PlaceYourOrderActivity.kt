package com.demo.foodorderanddeliveryappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.PlaceYourOrderAdapter
import com.demo.foodorderanddeliveryappkotlin.models.RestaurantModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class PlaceYourOrderActivity : BaseActivity() {
    private lateinit var restaurantModel: RestaurantModel
    private lateinit var placeOrderAdapter: PlaceYourOrderAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // UI Components
    private lateinit var switchDelivery: SwitchCompat
    private lateinit var inputName: EditText
    private lateinit var inputAddress: EditText
    private lateinit var inputCity: EditText
    private lateinit var inputState: EditText
    private lateinit var inputZip: EditText
    private lateinit var inputCardNumber: EditText
    private lateinit var inputCardExpiry: EditText
    private lateinit var inputCardPin: EditText
    private lateinit var cartItemsRecyclerView: RecyclerView
    private lateinit var tvSubtotalAmount: TextView
    private lateinit var tvDeliveryChargeAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnPlaceOrder: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)
        
        try {
            // Get restaurant model with proper Android Tiramisu support
            restaurantModel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("RestaurantModel", RestaurantModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("RestaurantModel")
            } ?: throw IllegalArgumentException("RestaurantModel must not be null")
                
            // Validate that the restaurant has items in cart
            val hasItemsInCart = restaurantModel.menus?.any { it?.totalInCart ?: 0 > 0 } ?: false
            if (!hasItemsInCart) {
                Toast.makeText(this, "No items in your cart", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            
            Log.d("PlaceYourOrderActivity", "Received restaurant model: ${restaurantModel.name}")
            
            initializeViews()
            setupRecyclerView()
            setupOrderSummary()
            setupDeliverySwitch()
            setupPlaceOrderButton()
        } catch (e: Exception) {
            Log.e("PlaceYourOrderActivity", "Error in onCreate", e)
            finish()
        }
    }

    private fun initializeViews() {
        switchDelivery = findViewById(R.id.switchDelivery)
        inputName = findViewById(R.id.inputName)
        inputAddress = findViewById(R.id.inputAddress)
        inputCity = findViewById(R.id.inputCity)
        inputState = findViewById(R.id.inputState)
        inputZip = findViewById(R.id.inputZip)
        inputCardNumber = findViewById(R.id.inputCardNumber)
        inputCardExpiry = findViewById(R.id.inputCardExpiry)
        inputCardPin = findViewById(R.id.inputCardPin)
        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView)
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount)
        tvDeliveryChargeAmount = findViewById(R.id.tvDeliveryChargeAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
    }

    private fun setupRecyclerView() {
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        placeOrderAdapter = PlaceYourOrderAdapter(
            restaurantModel.menus?.filterNotNull()?.filter { menu -> menu.totalInCart > 0 } ?: emptyList()
        )
        cartItemsRecyclerView.adapter = placeOrderAdapter
    }

    private fun setupOrderSummary() {
        val subtotal = restaurantModel.menus?.filterNotNull()?.sumOf { menu ->
            (menu.price * (menu.totalInCart ?: 0)).toDouble()
        } ?: 0.0
        val deliveryCharge = 5.0 // Fixed delivery charge
        val total = subtotal + deliveryCharge

        tvSubtotalAmount.text = String.format("$%.2f", subtotal)
        tvDeliveryChargeAmount.text = String.format("$%.2f", deliveryCharge)
        tvTotalAmount.text = String.format("$%.2f", total)
    }

    private fun setupDeliverySwitch() {
        switchDelivery.setOnCheckedChangeListener { _, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            inputAddress.visibility = visibility
            inputCity.visibility = visibility
            inputState.visibility = visibility
            inputZip.visibility = visibility
        }
    }

    private fun setupPlaceOrderButton() {
        btnPlaceOrder.setOnClickListener {
            if (validateForm()) {
                createOrder()
            }
        }
    }

    private fun validateForm(): Boolean {
        if (inputName.text.isNullOrBlank()) {
            inputName.error = "Name is required"
            return false
        }

        if (switchDelivery.isChecked) {
            if (inputAddress.text.isNullOrBlank()) {
                inputAddress.error = "Address is required"
                return false
            }
            if (inputCity.text.isNullOrBlank()) {
                inputCity.error = "City is required"
                return false
            }
            if (inputState.text.isNullOrBlank()) {
                inputState.error = "State is required"
                return false
            }
            if (inputZip.text.isNullOrBlank()) {
                inputZip.error = "ZIP is required"
                return false
            }
        }

        if (inputCardNumber.text.isNullOrBlank() || inputCardNumber.text.length != 16) {
            inputCardNumber.error = "Valid card number is required"
            return false
        }
        if (inputCardExpiry.text.isNullOrBlank() || inputCardExpiry.text.length != 6) {
            inputCardExpiry.error = "Valid expiry date (MMYYYY) is required"
            return false
        }
        if (inputCardPin.text.isNullOrBlank() || inputCardPin.text.length != 3) {
            inputCardPin.error = "Valid CVV is required"
            return false
        }

        return true
    }

    private fun createOrder() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("PlaceYourOrderActivity", "User not logged in")
            return
        }

        val order = hashMapOf(
            "userId" to currentUser.uid,
            "restaurantName" to restaurantModel.name,
            "customerName" to inputName.text.toString(),
            "deliveryAddress" to if (switchDelivery.isChecked) {
                "${inputAddress.text}, ${inputCity.text}, ${inputState.text} ${inputZip.text}"
            } else "Pickup",
            "items" to (restaurantModel.menus?.filterNotNull()?.map { menu ->
                hashMapOf(
                    "name" to menu.name,
                    "price" to menu.price,
                    "quantity" to menu.totalInCart
                )
            } ?: emptyList()),
            "subtotal" to (restaurantModel.menus?.filterNotNull()?.sumOf { menu ->
                menu.price.toDouble() * (menu.totalInCart ?: 0)
            } ?: 0.0),
            "deliveryCharge" to if (switchDelivery.isChecked) 5.0 else 0.0,
            "total" to ((restaurantModel.menus?.filterNotNull()?.sumOf { menu ->
                menu.price.toDouble() * (menu.totalInCart ?: 0)
            } ?: 0.0) + if (switchDelivery.isChecked) 5.0 else 0.0),
            "status" to "Placed",
            "timestamp" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        )

        firestore.collection("orders")
            .add(order)
            .addOnSuccessListener { documentReference ->
                Log.d("PlaceYourOrderActivity", "Order placed successfully")
                navigateTo(OrderConfirmationActivity::class.java) { intent ->
                    intent.putExtra("orderId", documentReference.id)
                }
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("PlaceYourOrderActivity", "Error placing order", e)
                Toast.makeText(this, "Failed to place order: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
} 