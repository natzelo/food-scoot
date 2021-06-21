package com.example.foodbunny.database

import androidx.room.Database
import androidx.room.RoomDatabase

// We create the database my passing in the table definition (aka entities) and their DAO this class

@Database(entities = [RestaurantEntity::class], version = 1)
abstract class RestaurantDatabase: RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

}