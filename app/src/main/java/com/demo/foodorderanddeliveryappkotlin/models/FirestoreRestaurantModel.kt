package com.demo.foodorderanddeliveryappkotlin.models

data class FirestoreRestaurantModel(
    val title: String = "",
    val address: String = "",
    val price: String = "",
    val thumbnail: String = "",
    val operatingHours: OperatingHours = OperatingHours(),
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val type: String = "",
    val openState: String = "Closed",
    val menus: List<MenuItem> = emptyList()
)

data class OperatingHours(
    val sunday: String = "",
    val monday: String = "",
    val tuesday: String = "",
    val wednesday: String = "",
    val thursday: String = "",
    val friday: String = "",
    val saturday: String = ""
)
