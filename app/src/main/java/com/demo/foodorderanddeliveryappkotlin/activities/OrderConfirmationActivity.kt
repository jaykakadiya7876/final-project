package com.demo.foodorderanddeliveryappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.OrderModel
import com.demo.foodorderanddeliveryappkotlin.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderConfirmationActivity : BaseActivity() {
    private lateinit var orderRepository: OrderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        orderRepository = OrderRepository()

        val orderId = intent.getStringExtra("orderId") ?: return

        val tvOrderId = findViewById<TextView>(R.id.tvOrderId)
        val tvOrderStatus = findViewById<TextView>(R.id.tvOrderStatus)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        tvOrderId.text = "Order #$orderId"
        
        lifecycleScope.launch {
            try {
                val order = orderRepository.getOrderById(orderId)
                order?.let { orderModel ->
                    tvOrderStatus.text = "Status: ${orderModel.status.capitalize()}"
                }
            } catch (e: Exception) {
                tvOrderStatus.text = "Status: Unknown"
            }
        }


        btnBackToHome.setOnClickListener {
            navigateTo(RestaurantListActivity::class.java)
        }
    }
}