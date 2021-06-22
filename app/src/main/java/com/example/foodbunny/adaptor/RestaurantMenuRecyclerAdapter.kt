package com.example.foodbunny.adaptor

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodbunny.R
import com.example.foodbunny.database.OrderDatabase
import com.example.foodbunny.database.OrderEntity
import com.example.foodbunny.model.Food
import com.google.gson.Gson

class RestaurantMenuRecyclerAdapter(val context: Context, private val itemList: ArrayList<Food>, private val restaurantId: String): RecyclerView.Adapter<RestaurantMenuRecyclerAdapter.RestaurantMenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_food_menu_row, parent, false)
        return RestaurantMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val food = itemList[position]
        holder.foodName.text = food.foodName
        holder.foodPrice.text = food.foodPrice

        holder.cartButton.setOnClickListener {
            if (holder.cartButton.text == "ADD") {
                val orderEntity = OrderDBAsyncTask(context, OrderEntity(restaurantId, "dummy"), 3).execute().get()
                if(orderEntity != null) {

                    val json:String = orderEntity.foodItems
                    val orderList: ArrayList<Food> = arrayListOf()
                    orderList.addAll(
                        Gson().fromJson(json, Array<Food>::class.java).asList()
                    )
                    val newItem = Food(food.foodId, food.foodName, food.foodPrice)
                    orderList.add(newItem)

                    //Clear the table
                    OrderDBAsyncTask(context, OrderEntity("dummy", "dummy"), 2).execute().get()

                    //Insert New Item
                    val newOrderEntity = OrderEntity(restaurantId, Gson().toJson(orderList))
                    OrderDBAsyncTask(context, newOrderEntity, 1).execute()

                } else {
                    val orderList = arrayOf(Food(food.foodId, food.foodName, food.foodPrice))
                    val newOrderEntity = OrderEntity(restaurantId, Gson().toJson(orderList))
                    OrderDBAsyncTask(context, newOrderEntity, 1).execute().get()
                }

                holder.cartButton.text = "REMOVE"

            } else if(holder.cartButton.text == "REMOVE") {
                val orderEntity = OrderDBAsyncTask(context, OrderEntity(restaurantId, "dummy"), 3).execute().get()
                if(orderEntity == null) {

                }

                val json:String = orderEntity.foodItems
                val orderList: ArrayList<Food> = arrayListOf()
                orderList.addAll(
                    Gson().fromJson(json, Array<Food>::class.java).asList()
                )

                orderList.remove(Food(food.foodId, food.foodName, food.foodPrice))
                //Clear the table
                OrderDBAsyncTask(context, OrderEntity("dummy", "dummy"), 2).execute().get()

                //Insert New Item
                val newOrderEntity = OrderEntity(restaurantId, Gson().toJson(orderList))
                OrderDBAsyncTask(context, newOrderEntity, 1).execute().get()

                holder.cartButton.text = "ADD"
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class RestaurantMenuViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val foodName: TextView = view.findViewById(R.id.food_name)
        val foodPrice: TextView = view.findViewById(R.id.food_price)
        val cartButton: Button = view.findViewById(R.id.cart_btn)
    }

    class OrderDBAsyncTask(val context: Context, private val orderEntity: OrderEntity, private val mode: Int): AsyncTask<Void, Void, OrderEntity>() {

        /**
         * Mode 1 - Insert
         * Mode 2 - Delete
         * Mode 3 - get
         */


        private val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()

        override fun doInBackground(vararg params: Void?): OrderEntity? {

            when(mode) {
                1-> {
                    db.orderDao().insertOrder(orderEntity)
                    db.close()

                    return OrderEntity("dummy", "dummy")
                }
                2 -> {
                    db.orderDao().deleteOrder()
                    db.close()
                    return OrderEntity("dummy", "dummy")
                }
                3-> {

                    val result = db.orderDao().getOrder(orderEntity.restaurantId)
                    db.close()
                    return result
                }
            }

            return OrderEntity("dummy", "dummy")
        }
    }
}