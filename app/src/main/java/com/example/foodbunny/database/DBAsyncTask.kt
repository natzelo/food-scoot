package com.example.foodbunny.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class DBAsyncTask(val context: Context, private val restaurantEntity: RestaurantEntity, private val mode: Int): AsyncTask<Void, Void, Boolean>() {
    /**
     * Mode 1 - Check if the book is favourite or not
     * Mode 2 - Insert book to the database
     * Mode 3 - Remove book from the database
     */

    private val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db" ).build()
    override fun doInBackground(vararg params: Void?): Boolean {
        when(mode) {
            1 -> {
                val restaurant: RestaurantEntity? =  db.restaurantDao().getRestaurantById(restaurantEntity.restaurantId)
                db.close()
                return restaurant != null
            }

            2-> {
                db.restaurantDao().insertRestaurant(restaurantEntity)
                db.close()
                return true
            }
            3 -> {
                db.restaurantDao().deleteRestaurant(restaurantEntity)
                db.close()
                return true
            }
        }

        return false
    }

}