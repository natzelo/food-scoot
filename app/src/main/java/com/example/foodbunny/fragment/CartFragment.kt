package com.example.foodbunny.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.CartAdaptor
import com.example.foodbunny.adaptor.RestaurantMenuRecyclerAdapter
import com.example.foodbunny.database.OrderEntity
import com.example.foodbunny.databinding.FragmentCartBinding
import com.example.foodbunny.model.Food
import com.google.gson.Gson

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)

        val restaurantId = arguments?.getString("restaurant_id")
        Toast.makeText(activity, "Restaurant is $restaurantId", Toast.LENGTH_SHORT).show()

        val layoutManager = LinearLayoutManager(activity)

        val orderEntity: OrderEntity = RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(activity as Context, OrderEntity(restaurantId as String, "dummy"), 3).execute().get()

        val orderList: ArrayList<Food> = arrayListOf()

        orderList.addAll(
            Gson().fromJson(orderEntity.foodItems, Array<Food>::class.java).asList()
        )
        Log.i("DEBUG", orderList.toString())

        val cartAdaptor = CartAdaptor(activity as Context, orderList)

        binding.cartRecycler.layoutManager = layoutManager
        binding.cartRecycler.adapter = cartAdaptor

        (activity as AppCompatActivity).supportActionBar?.title = "Cart"
        return binding.root


    }





}