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
import com.example.foodbunny.adaptor.RestaurantMenuRecyclerAdapter
import com.example.foodbunny.databinding.FragmentRestaurantMenuBinding
import com.example.foodbunny.model.Food

class FragmentRestaurantMenu : Fragment() {

    private val foods = arrayListOf(
        Food("Potato", "200"),
        Food("Chicken Bhuna", "400"),
        Food("Cheese", "120")
    )

    private lateinit var  binding: FragmentRestaurantMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantMenuBinding.inflate(layoutInflater)
        val bundle:Bundle? = arguments
        Log.i("IN TRACK", "bundle ${bundle.toString()}")
        val restaurantId = bundle?.getString("restaurant_id")
        Toast.makeText(activity as Context, "Restaurant Id is $restaurantId", Toast.LENGTH_SHORT).show()

        val layoutManager = LinearLayoutManager(activity)
        val restaurantMenuAdapter = RestaurantMenuRecyclerAdapter(activity as Context, foods)

        binding.restaurantMenuRecycler.layoutManager = layoutManager
        binding.restaurantMenuRecycler.adapter = restaurantMenuAdapter

        return binding.root
    }


}