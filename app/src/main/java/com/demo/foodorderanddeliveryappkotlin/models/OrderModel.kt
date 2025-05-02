package com.demo.foodorderanddeliveryappkotlin.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class OrderModel(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val deliveryCharge: Double = 0.0,
    val deliveryAddress: String? = null,
    val deliveryCity: String? = null,
    val deliveryState: String? = null,
    val deliveryZip: String? = null,
    val customerName: String = "",
    val status: String = "pending", // pending, preparing, ready, delivered
    val createdAt: Timestamp = Timestamp.now()
) {
    // Inner class for OrderItem to be used in the app
    data class OrderItem(
        val name: String = "",
        val price: Double = 0.0,
        val quantity: Int = 0,
        val imageUrl: String = ""
    ) : Serializable
}
