package com.demo.foodorderanddeliveryappkotlin.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.OrderItemsAdapter
import com.demo.foodorderanddeliveryappkotlin.models.OrderModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailsActivity : BaseActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var orderItemsAdapter: OrderItemsAdapter
    private lateinit var recyclerView: RecyclerView
    
    // Order status card views
    private lateinit var orderIdText: TextView
    private lateinit var orderDetailsText: TextView
    private lateinit var expandButton: ImageButton
    private lateinit var timelineLayout: View
    
    // Price details views
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDeliveryCharge: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        db = FirebaseFirestore.getInstance()
        initializeViews()

        val orderId = intent.getStringExtra("orderId") ?: run {
            finish()
            return
        }

        loadOrderDetails(orderId)
    }

    private fun initializeViews() {
        // Initialize order status card views
        val orderStatusCard = findViewById<View>(R.id.orderStatusCard)
        orderIdText = orderStatusCard.findViewById(R.id.orderIdText)
        orderDetailsText = orderStatusCard.findViewById(R.id.orderDetailsText)
        expandButton = orderStatusCard.findViewById(R.id.expandButton)
        timelineLayout = orderStatusCard.findViewById(R.id.timelineLayout)
        
        // Set up expand/collapse functionality
        expandButton.setOnClickListener {
            if (timelineLayout.visibility == View.VISIBLE) {
                timelineLayout.visibility = View.GONE
                expandButton.setImageResource(R.drawable.ic_expand)
            } else {
                timelineLayout.visibility = View.VISIBLE
                expandButton.setImageResource(R.drawable.ic_collapse)
            }
        }

        // Initialize price details views
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvDeliveryCharge = findViewById(R.id.tvDeliveryCharge)
        tvTotal = findViewById(R.id.tvTotal)

        // Initialize recycler view
        recyclerView = findViewById(R.id.recyclerViewOrderItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderItemsAdapter = OrderItemsAdapter()
        recyclerView.adapter = orderItemsAdapter
    }

    private fun loadOrderDetails(orderId: String) {
        db.collection("orders").document(orderId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val order = document.toObject(OrderModel::class.java)
                    order?.let { displayOrderDetails(it) }
                }
            }
    }

    private fun displayOrderDetails(order: OrderModel) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val dateFormat = SimpleDateFormat("h:mm a, d MMM yyyy", Locale.getDefault())

        // Update order status card
        orderIdText.text = "Order #${order.id}"
        orderDetailsText.text = "${order.items.size} Items · ${order.status.capitalize()}"

        // Set price details
        tvSubtotal.text = currencyFormat.format(order.totalAmount)
        tvDeliveryCharge.text = currencyFormat.format(order.deliveryCharge)
        tvTotal.text = currencyFormat.format(order.totalAmount + order.deliveryCharge)

        // Update recycler view with order items
        orderItemsAdapter.updateItems(order.items)
    }
} 