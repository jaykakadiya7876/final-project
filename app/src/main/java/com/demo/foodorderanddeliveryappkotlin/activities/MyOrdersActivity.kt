package com.demo.foodorderanddeliveryappkotlin.activities

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.MyOrdersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyOrdersActivity : BaseActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var ordersAdapter: MyOrdersAdapter
    private lateinit var ordersRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        setSelectedItem(R.id.nav_orders)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        ordersRecyclerView = findViewById(R.id.recyclerViewOrders)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = MyOrdersAdapter()
        ordersRecyclerView.adapter = ordersAdapter
    }

    private fun loadOrders() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Error loading orders: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val orders = mutableListOf<Map<String, Any>>()
                snapshot?.documents?.forEach { doc ->
                    // Create a mutable map from the document data
                    val orderData = doc.data?.toMutableMap() ?: mutableMapOf()
                    // Add the document ID to the map
                    orderData["id"] = doc.id
                    orders.add(orderData)
                }
                ordersAdapter.updateOrders(orders)
            }
    }
} 