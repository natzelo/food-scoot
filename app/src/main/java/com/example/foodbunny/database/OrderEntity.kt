package com.example.foodbunny.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
class OrderEntity (
    @PrimaryKey val restaurantId: String,
    @ColumnInfo(name = "food_items") val foodItems: String
)