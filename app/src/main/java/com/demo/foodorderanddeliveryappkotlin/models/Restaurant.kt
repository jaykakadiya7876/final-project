package com.demo.foodorderanddeliveryappkotlin.models

data class Restaurant(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val deliveryTime: String = "",
    val deliveryFee: Double = 0.0,
    val types: List<String> = listOf()
) 