package com.example.foodbunny.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodbunny.R

class FragmentRestaurantMenu : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle:Bundle? = arguments
        Log.i("IN TRACK", "bundle ${bundle.toString()}")
        val restaurantId = bundle?.getString("restaurant_id")
        Toast.makeText(activity as Context, "Restaurant Id is $restaurantId", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_restaurant_menu, container, false)
    }


}