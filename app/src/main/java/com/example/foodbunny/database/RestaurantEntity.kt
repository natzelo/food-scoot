package com.example.foodbunny.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Retaurant Table is defined here

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val restaurantId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rating") val rating: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "dish") val dish: String
)