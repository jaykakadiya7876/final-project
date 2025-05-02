package com.demo.foodorderanddeliveryappkotlin.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.repository.OrderRepository
import kotlinx.coroutines.launch

class TrackOrderActivity : AppCompatActivity() {
    private lateinit var orderRepository: OrderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_order)

        orderRepository = OrderRepository()

        val orderId = intent.getStringExtra("orderId") ?: return

        val tvOrderId = findViewById<TextView>(R.id.tvOrderId)
        val tvOrderStatus = findViewById<TextView>(R.id.tvOrderStatus)

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
    }
} 