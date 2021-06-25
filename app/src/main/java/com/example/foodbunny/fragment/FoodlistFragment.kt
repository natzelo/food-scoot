package com.example.foodbunny.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.FoodlistRecyclerAdapter
import com.example.foodbunny.database.RestaurantDatabase
import com.example.foodbunny.database.RestaurantEntity
import com.example.foodbunny.databinding.FragmentFoodlistBinding
import com.example.foodbunny.model.Restaurant
import com.example.foodbunny.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap


class FoodlistFragment : Fragment() {

    private lateinit var binding:FragmentFoodlistBinding
    private lateinit var layoutManager: LinearLayoutManager
    private  var itemList: ArrayList<Restaurant> = arrayListOf()
    private lateinit var recyclerAdapter: RecyclerView.Adapter<FoodlistRecyclerAdapter.FoodlistViewHolder>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodlistBinding.inflate(layoutInflater)
        binding.progressLayout.visibility =  View.VISIBLE
        setHasOptionsMenu(true)
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

                        if(activity!= null) {
                            recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)
                            binding.recyclerView.adapter = recyclerAdapter
                            binding.recyclerView.layoutManager = layoutManager
                        }


                    }
                } else {
                    Toast.makeText(activity, "Some Error", Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                if(activity != null) {
                    Toast.makeText(activity, "Some Error", Toast.LENGTH_SHORT).show()
                }
            }


        }, Response.ErrorListener {
            if(activity != null) {
                Toast.makeText(activity, "Some Error", Toast.LENGTH_SHORT).show()
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
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.sort_menu) {
            itemList.sortWith { restaurant1, restaurant2 ->
                restaurant1.rating.compareTo(restaurant2.rating, true)
            }
            itemList.reverse()

            recyclerAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }

}