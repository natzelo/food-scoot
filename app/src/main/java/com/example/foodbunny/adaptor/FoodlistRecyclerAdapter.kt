package com.example.foodbunny.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbunny.R
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
        Picasso.get().load(restaurant.dish).into(holder.dish)
    }
}