package com.example.foodbunny.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbunny.R
import com.example.foodbunny.model.OrderHistory

class OrderHistoryAdaptor(val context: Context, private val itemList: ArrayList<OrderHistory>): RecyclerView.Adapter<OrderHistoryAdaptor.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder( view : View): RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.restauant_name_order_history)
        val date: TextView = view.findViewById(R.id.date_order_history)
        val recyclerView: RecyclerView = view.findViewById(R.id.order_history_row_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_hisrtory_single_row, parent, false)
        return  OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistory = itemList[position]
        holder.restaurantName.text = orderHistory.restaurantName
        holder.date.text = orderHistory.date
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.recyclerView.adapter = CartAdaptor(context, orderHistory.foods)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }
}