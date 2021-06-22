package com.example.foodbunny.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {

    @Insert
    fun insertOrder(orderEntity: OrderEntity)

    @Query("DELETE from orders")
    fun deleteOrder()

    @Query("SELECT * FROM orders WHERE restaurantId = :id")
    fun getOrder(id: String): OrderEntity?
}