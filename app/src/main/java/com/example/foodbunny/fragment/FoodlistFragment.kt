package com.example.foodbunny.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.adaptor.FoodlistRecyclerAdapter
import com.example.foodbunny.database.RestaurantDatabase
import com.example.foodbunny.database.RestaurantEntity
import com.example.foodbunny.databinding.FragmentFoodlistBinding
import com.example.foodbunny.model.Restaurant
import com.example.foodbunny.util.ConnectionManager
import org.json.JSONException
import java.util.ArrayList


class FoodlistFragment : Fragment() {

    private lateinit var binding:FragmentFoodlistBinding
    private lateinit var layoutManager: LinearLayoutManager
    private  var itemList: ArrayList<Restaurant> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodlistBinding.inflate(layoutInflater)
        binding.progressLayout.visibility =  View.VISIBLE
        // check internet connection
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

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

            try {
                val data = it.getJSONObject("data")
                val isGood = data.getBoolean("success")
                if(isGood) {
                    binding.progressLayout.visibility = View.GONE
                    val dataArray = data.getJSONArray("data")
                    for( i in 0 until dataArray.length()) {
                        val restaurantObj = dataArray.getJSONObject(i)
                        val restaurant = Restaurant(
                            restaurantObj.getString("id"),
                            restaurantObj.getString("name"),
                            restaurantObj.getString("rating"),
                            restaurantObj.getString("cost_for_one"),
                            restaurantObj.getString("image_url")
                        )
                        itemList.add(restaurant)


                        val recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)

                        binding.recyclerView.adapter = recyclerAdapter
                        binding.recyclerView.layoutManager = layoutManager

                    }
                } else {
                    Toast.makeText(activity, "Some Error", Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                Toast.makeText(activity, "Some Error", Toast.LENGTH_SHORT).show()
            }


        }, Response.ErrorListener {
                Toast.makeText(activity, "Some Error", Toast.LENGTH_SHORT).show()
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


    class DBAsyncTask(val context: Context, private val restaurantEntity: RestaurantEntity, private val mode: Int): AsyncTask<Void, Void, Boolean>() {
        /**
         * Mode 1 - Check if the book is favourite or not
         * Mode 2 - Insert book to the database
         * Mode 3 - Remove book from the database
         */

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db" ).build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode) {
                1 -> {
                    val restaurant: RestaurantEntity? =  db.restaurantDao().getRestaurantById(restaurantEntity.restaurantId.toString())
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

}