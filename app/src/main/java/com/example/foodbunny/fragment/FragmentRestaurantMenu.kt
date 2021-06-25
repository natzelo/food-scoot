package com.example.foodbunny.fragment

import android.content.Context
import android.os.Bundle
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
import com.example.foodbunny.adaptor.RestaurantMenuRecyclerAdapter
import com.example.foodbunny.database.OrderEntity
import com.example.foodbunny.databinding.FragmentRestaurantMenuBinding
import com.example.foodbunny.model.Food
import com.google.gson.Gson
import org.json.JSONException

class FragmentRestaurantMenu : Fragment() {

    private val foods: ArrayList<Food> = arrayListOf()
    private lateinit var  binding: FragmentRestaurantMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantMenuBinding.inflate(layoutInflater)
        binding.restaurantMenuProgressLayout.visibility = View.VISIBLE
        val bundle:Bundle? = arguments
        Log.i("IN TRACK", "bundle ${bundle.toString()}")
        val restaurantId = bundle?.getString("restaurant_id")
        Toast.makeText(activity as Context, "Restaurant Id is $restaurantId", Toast.LENGTH_SHORT).show()
        val layoutManager = LinearLayoutManager(activity)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            try {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if(success) {
                    binding.restaurantMenuProgressLayout.visibility = View.GONE
                    val dataArray = data.getJSONArray("data")

                    for(i in 0 until dataArray.length()) {
                        val foodJSON = dataArray.getJSONObject(i)

                        val food = Food(
                            foodJSON.getString("id"),
                            foodJSON.getString("name"),
                            foodJSON.getString("cost_for_one")
                        )

                        foods.add(food)
                        val restaurantMenuAdapter = RestaurantMenuRecyclerAdapter(activity as Context, foods, restaurantId as String)

                        binding.restaurantMenuRecycler.layoutManager = layoutManager
                        binding.restaurantMenuRecycler.adapter = restaurantMenuAdapter
                    }
                } else {
                    Toast.makeText(activity as Context,"Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                if(activity != null) {
                    Toast.makeText(activity, "Some Error", Toast.LENGTH_SHORT).show()
                }
            }
        }, Response.ErrorListener {
            if(activity != null) {
                Toast.makeText(activity, "Volley Error", Toast.LENGTH_SHORT).show()
            }
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "5fc57ce44a41aa"
                return headers
            }
        }
        queue.add(jsonObjectRequest)

        binding.proceedToCard.setOnClickListener {

            if(activity == null) {
                Log.i( "DEBUG","Activity NULL")
            } else {
                val executor = RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(activity as Context, OrderEntity(restaurantId as String, "dummy"), 3)
                val getter = executor.execute()
                val orderEntity = getter.get()

                if(orderEntity == null) {
                    Log.i("DEBUG","Order entity null")
                    Toast.makeText(activity,"Please select at least one dish", Toast.LENGTH_SHORT).show()
                } else {
                    val orderList: ArrayList<Food> = arrayListOf()
                    orderList.addAll(
                        Gson().fromJson(orderEntity.foodItems, Array<Food>::class.java).asList()
                    )
                    if(orderList.size == 0) {
                        Toast.makeText(
                            activity,
                            "Please select at least one dish",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val bundle = Bundle()
                        bundle.putString("restaurant_id", restaurantId)
                        val cartFragment = CartFragment()
                        cartFragment.arguments = bundle
                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_layout_restaurant_activity, cartFragment).commit()

                    }

                }
            }


        }


        return binding.root
    }


}