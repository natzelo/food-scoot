package com.example.foodbunny.model

data class OrderHistory(
    val restaurantName: String,
    val date: String,
    val foods: ArrayList<Food>
)
