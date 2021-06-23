package com.example.foodbunny.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbunny.R
import com.example.foodbunny.model.CartItem
import com.example.foodbunny.model.Food

class CartAdaptor(val context: Context, private  val itemList: ArrayList<Food>): RecyclerView.Adapter<CartAdaptor.CartViewHolder>() {

    class CartViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val foodItem: TextView = view.findViewById(R.id.food_item_cart)
        val foodPrice: TextView = view.findViewById(R.id.food_price_cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_single_row, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val food: Food= itemList[position]
        holder.foodItem.text = food.foodName
        holder.foodPrice.text = food.foodPrice
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}