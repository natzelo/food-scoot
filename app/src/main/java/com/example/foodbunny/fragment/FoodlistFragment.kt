package com.example.foodbunny.fragment

import android.app.AlertDialog
import android.app.VoiceInteractor
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.FoodlistRecyclerAdapter
import com.example.foodbunny.databinding.FragmentFoodlistBinding
import com.example.foodbunny.model.Restaurant
import com.example.foodbunny.util.ConnectionManager
import java.util.ArrayList


class FoodlistFragment : Fragment() {

    private lateinit var binding:FragmentFoodlistBinding
    private lateinit var layoutManager: LinearLayoutManager
    private  var itemList: ArrayList<Restaurant> = arrayListOf(Restaurant("name 1", "price 1", "rating 1", R.drawable.envelope), Restaurant("name 2", "rating 2", "price 2", R.drawable.envelope))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodlistBinding.inflate(layoutInflater)

        // check internet connection here
        if(!ConnectionManager().checkConnectivity(activity as Context)) {
            // there is no internet internet
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection found")
            dialog.setPositiveButton("Open Settings"){ _, _ ->
                startActivity(Intent(Settings.ACTION_SETTINGS))
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                    activity?.finishAffinity()
            }

            dialog.create()
            dialog.show()
        }

        layoutManager = LinearLayoutManager(activity)
        val recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)

        binding.recyclerView.adapter = recyclerAdapter
        binding.recyclerView.layoutManager = layoutManager

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            println("Response is $it")
//            val success = it.getBoolean("success")
//            if(success) {
//                val dataArray = data.getJSONArray("data")
//                for( i in 0 until dataArray.length()) {
//                    val restaurantObj = dataArray.getJSONObject(i)
//                    val restaurant = Restaurant(
//                        restaurantObj.getString("name"),
//                        restaurantObj.getString("rating"),
//                        restaurantObj.getString("cost_for_one"),
//                        R.drawable.envelope
//                    )
//                    itemList.add(restaurant)
//                    val recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)
//
//                    binding.recyclerView.adapter = recyclerAdapter
//                    binding.recyclerView.layoutManager = layoutManager
//                }
//            } else {
//                Toast.makeText(activity, "Some Error", Toast.LENGTH_LONG).show()
//            }
//
            val data = it.getJSONObject("data")
            val isGood = data.getBoolean("success")
            if(isGood) {
                val dataArray = data.getJSONArray("data")
                for( i in 0 until dataArray.length()) {
                    val restaurantObj = dataArray.getJSONObject(i)
                    val restaurant = Restaurant(
                        restaurantObj.getString("name"),
                        restaurantObj.getString("rating"),
                        restaurantObj.getString("cost_for_one"),
                        R.drawable.envelope
                    )
                    itemList.add(restaurant)
                    val recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)

                    binding.recyclerView.adapter = recyclerAdapter
                    binding.recyclerView.layoutManager = layoutManager
                }
            } else {
                Toast.makeText(activity, "Some Error", Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener {
                println("Error is $it")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "5fc57ce44a41aa"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
        return binding.root
    }

}