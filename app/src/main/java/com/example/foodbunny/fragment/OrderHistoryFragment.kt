package com.example.foodbunny.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.OrderHistoryAdaptor
import com.example.foodbunny.databinding.FragmentOrderHistoryBinding
import com.example.foodbunny.model.Food
import com.example.foodbunny.model.OrderHistory
import com.google.gson.Gson


class OrderHistoryFragment : Fragment() {

    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderHistoryBinding.inflate(layoutInflater)
        sharedPreferences = activity?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)!!

        val userId = sharedPreferences.getString("user_id", "").toString()
        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener {

                val data = it.getJSONObject("data")
                if(data.getBoolean("success")) {
                    val dataObj = data.getJSONArray("data")
                    val orderList: ArrayList<OrderHistory> = arrayListOf()
                    for (i in 0 until dataObj.length()) {
                        val jsonOrderHistory = dataObj.getJSONObject(i)
                        val foodList: ArrayList<Food> = arrayListOf()
                        val foodJsonArray = jsonOrderHistory.getJSONArray("food_items")
                        for(j in 0 until foodJsonArray.length()) {
                            val foodJsonObj = foodJsonArray.getJSONObject(j)
                            val food = Food(
                                foodJsonObj.getString("food_item_id"),
                                foodJsonObj.getString("name"),
                                foodJsonObj.getString("cost")
                            )
                            foodList.add(food)
                        }

                        val orderHistory = OrderHistory(
                            jsonOrderHistory.getString("restaurant_name"),
                            jsonOrderHistory.getString("order_placed_at"),
                            foodList

                        )

                        orderList.add(orderHistory)
                    }
                    val layoutManager = LinearLayoutManager(context)
                    binding.orderHistoryRecycler.layoutManager = layoutManager
                    binding.orderHistoryRecycler.adapter = OrderHistoryAdaptor(activity as Context, orderList)
                    val dividerItemDecoration = DividerItemDecoration(binding.orderHistoryRecycler.context, layoutManager.orientation)
                    binding.orderHistoryRecycler.addItemDecoration(dividerItemDecoration)
                    Log.i("DEBUG", orderList.toString())

                } else {
                    Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }


        }, Response.ErrorListener {
                Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "5fc57ce44a41aa"
                return headers
            }
        }

        val queue = Volley.newRequestQueue(context)
        queue.add(jsonObjectRequest)
        return binding.root
    }



}