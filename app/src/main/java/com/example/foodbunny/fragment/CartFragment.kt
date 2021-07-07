package com.example.foodbunny.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.CartAdaptor
import com.example.foodbunny.adaptor.RestaurantMenuRecyclerAdapter
import com.example.foodbunny.database.OrderEntity
import com.example.foodbunny.databinding.FragmentCartBinding
import com.example.foodbunny.model.Food
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var sharedPreferences: SharedPreferences
    private  var restaurantId: String = ""
    private var userId: String? = ""
    private var sum = 0
    private val orderList: ArrayList<Food> = arrayListOf()


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(context)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        if(activity != null) {
            sharedPreferences =
                activity?.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)!!
        }




        restaurantId = arguments?.getString("restaurant_id") as String
        userId = sharedPreferences.getString("user_id", "").toString()
        Log.i("DEBUG", "cart user id is ${sharedPreferences.all}")
        Log.i("DEBUG", "cart user id is $userId")

        val layoutManager = LinearLayoutManager(activity)

        val orderEntity: OrderEntity = RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(activity as Context, OrderEntity(restaurantId, "dummy"), 3).execute().get()



        orderList.addAll(
            Gson().fromJson(orderEntity.foodItems, Array<Food>::class.java).asList()
        )
        Log.i("DEBUG", orderList.toString())

        val cartAdaptor = CartAdaptor(activity as Context, orderList)

        binding.cartRecycler.layoutManager = layoutManager
        binding.cartRecycler.adapter = cartAdaptor

        (activity as AppCompatActivity).supportActionBar?.title = "Cart"

        orderList.forEach { food: Food ->
            sum += food.foodPrice.toInt()
        }
        binding.totalSum.text = sum.toString()
        binding.placeOrder.setOnClickListener {
            makeRequest()
        }

        return binding.root


    }


    private fun makeRequest() {

        val foodJsonArray = JSONArray()
        orderList.forEach { food: Food ->
           val obj = JSONObject()
            obj.put("food_item_id", food.foodId)
            foodJsonArray.put(obj)
        }

        val jsonRequest = JSONObject()
        jsonRequest.put("user_id", userId)
        jsonRequest.put("restaurant_id", restaurantId)
        jsonRequest.put("total_cost", sum.toString())
        jsonRequest.put("food", foodJsonArray)

        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonRequest,
            Response.Listener {
                Log.i("DEBUG", it.toString())
                val data = it.getJSONObject("data")
                if(data.getBoolean("success")) {

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.frame_layout_restaurant_activity, SuccessFragment())?.commit()


                } else {
                    Toast.makeText(activity, "Couldn't place order", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(activity, "Couldn't place order", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "5fc57ce44a41aa"
                return headers
            }
        }

        val queue = Volley.newRequestQueue(activity as Context)
        queue.add(jsonObjectRequest)
    }


}