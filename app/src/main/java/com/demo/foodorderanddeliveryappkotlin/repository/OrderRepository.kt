package com.demo.foodorderanddeliveryappkotlin.repository

import com.demo.foodorderanddeliveryappkotlin.models.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val ordersCollection = firestore.collection("orders")

    suspend fun createOrder(order: OrderModel): String {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val orderWithUserId = order.copy(userId = userId)
        
        val document = ordersCollection.add(orderWithUserId).await()
        return document.id
    }    suspend fun getOrderById(orderId: String): OrderModel? {
        return ordersCollection.document(orderId).get().await().toObject(OrderModel::class.java)
    }

    suspend fun getUserOrders(): List<OrderModel> {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        return ordersCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(OrderModel::class.java)
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        ordersCollection.document(orderId)
            .update("status", status)
            .await()
    }
} 