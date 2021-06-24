package com.example.foodbunny.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// The interface that interacts with the tables in the database

@Dao
interface RestaurantDao {

    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    fun getAllRestaurants(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE restaurantId = :id")
    fun getRestaurantById(id: String): RestaurantEntity?

    @Query("DELETE FROM restaurants")
    fun nukeTable()
}