package com.demo.foodorderanddeliveryappkotlin.models

data class FavoriteMenu(
    val id: String,
    val name: String,
    val description: String,
    val price: Float,
    val imageUrl: String,
    val restaurantId: String,
    val restaurantName: String
) 