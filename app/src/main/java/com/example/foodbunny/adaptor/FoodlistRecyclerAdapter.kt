package com.example.foodbunny.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbunny.R
import com.example.foodbunny.database.DBAsyncTask
import com.example.foodbunny.database.RestaurantEntity
import com.example.foodbunny.model.Restaurant
import com.squareup.picasso.Picasso

class FoodlistRecyclerAdapter(val context : Context, private val itemList: ArrayList<Restaurant> ) : RecyclerView.Adapter<FoodlistRecyclerAdapter.FoodlistViewHolder>() {

    // a cast-class which defines the various dynamic pieces within the view
    // so that when we create the view holder and give our view to it, it knows what elements
    // are dynamic in the view
    class FoodlistViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val restaurantName : TextView =  view.findViewById(R.id.restaurant_name)
        val rating: TextView = view.findViewById(R.id.rating)
        val price: TextView = view.findViewById(R.id.price_plate)
        val dish: ImageView = view.findViewById(R.id.dish_image)
        val likeButton: ImageView = view.findViewById(R.id.like_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_view_single_row, parent, false)

        // here we tell the adaptor that this is the your view holder
        return FoodlistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FoodlistViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.name
        holder.price.text = restaurant.price
        holder.rating.text = restaurant.rating
        Picasso.get().load(restaurant.dish).error(R.drawable.logo_red).into(holder.dish)

        val restaurantEntity = RestaurantEntity(restaurant.restaurantId,
            restaurant.name,
            restaurant.rating,
            restaurant.price,
            restaurant.dish)

        if (DBAsyncTask(context, restaurantEntity, 1 ).execute().get()) {
            holder.likeButton.setImageResource(R.drawable.ic_fav_orange)
            Log.i("DEBUG", "restaurant is liked")
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_fav_outline)
            Log.i("DEBUG", "restaurant is not liked")
        }

        holder.likeButton.setOnClickListener {
            if(DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val result = DBAsyncTask(context, restaurantEntity, 3).execute().get()

                if(result) {
                    Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
                    holder.likeButton.setImageResource(R.drawable.ic_fav_outline)
                } else {
                    Toast.makeText(context, "Could not remove from favorites", Toast.LENGTH_SHORT).show()
                }

            } else {
                val result = DBAsyncTask(context, restaurantEntity, 2).execute().get()

                if(result) {
                    Toast.makeText(context, "Interested into favourites", Toast.LENGTH_SHORT).show()
                    holder.likeButton.setImageResource(R.drawable.ic_fav_orange)
                } else {
                    Toast.makeText(context, "Could not insert into favorites", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }
}