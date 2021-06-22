package com.example.foodbunny.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbunny.R
import com.example.foodbunny.model.Food

class RestaurantMenuRecyclerAdapter(val context: Context, private val itemList: ArrayList<Food>): RecyclerView.Adapter<RestaurantMenuRecyclerAdapter.RestaurantMenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_food_menu_row, parent, false)
        return RestaurantMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.foodName.text = restaurant.foodName
        holder.foodPrice.text = restaurant.foodPrice
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class RestaurantMenuViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val foodName: TextView = view.findViewById(R.id.food_name)
        val foodPrice: TextView = view.findViewById(R.id.food_price)
        val cartButton: Button = view.findViewById(R.id.cart_btn)
    }
}