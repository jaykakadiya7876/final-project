package com.demo.foodorderanddeliveryappkotlin.repository

import com.demo.foodorderanddeliveryappkotlin.models.FirestoreRestaurantModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RestaurantRepository {
    private val db = FirebaseFirestore.getInstance()
    private val restaurantsCollection = db.collection("Restaurant")
    
    suspend fun getAllRestaurants(): List<FirestoreRestaurantModel> {
        return try {
            val snapshot = restaurantsCollection.get().await()
            val result = snapshot.documents.mapNotNull { document ->
                document.toObject(FirestoreRestaurantModel::class.java)
            }
            android.util.Log.d("RestaurantRepository", "Retrieved ${result.size} restaurants")
            result
        } catch (e: Exception) {
            // Log error details
            android.util.Log.e("RestaurantRepository", "Error retrieving restaurants", e)
            emptyList()
        }
    }
    
    suspend fun searchRestaurants(query: String): List<FirestoreRestaurantModel> {
        return try {
            // Get all restaurants and filter in-memory
            // This approach is more efficient for small datasets and allows for more complex filtering
            val restaurants = getAllRestaurants()
            
            if (query.isBlank()) {
                return restaurants
            }
            
            // Filter restaurants by query
            val lowercaseQuery = query.lowercase()
            restaurants.filter { restaurant ->
                restaurant.title.lowercase().contains(lowercaseQuery) ||
                restaurant.address.lowercase().contains(lowercaseQuery) ||
                restaurant.type.lowercase().contains(lowercaseQuery)
            }
        } catch (e: Exception) {
            android.util.Log.e("RestaurantRepository", "Error searching restaurants", e)
            emptyList()
        }
    }
    
    suspend fun filterRestaurantsByType(type: String): List<FirestoreRestaurantModel> {
        return try {
            val restaurants = getAllRestaurants()
            restaurants.filter { it.type == type }
        } catch (e: Exception) {
            android.util.Log.e("RestaurantRepository", "Error filtering restaurants by type", e)
            emptyList()
        }
    }
    
    suspend fun filterRestaurantsByRating(minRating: Double): List<FirestoreRestaurantModel> {
        return try {
            val restaurants = getAllRestaurants()
            restaurants.filter { it.rating >= minRating }
        } catch (e: Exception) {
            android.util.Log.e("RestaurantRepository", "Error filtering restaurants by rating", e)
            emptyList()
        }
    }
}
