package com.example.foodbunny.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.FoodlistRecyclerAdapter
import com.example.foodbunny.databinding.FragmentFoodlistBinding
import com.example.foodbunny.model.Restaurant
import java.util.ArrayList


class FoodlistFragment : Fragment() {

    private lateinit var binding:FragmentFoodlistBinding
    private lateinit var layoutManager: LinearLayoutManager
    private val itemList: ArrayList<Restaurant> = arrayListOf(Restaurant("Name 1", "rating 1", "price 1", R.drawable.envelope), Restaurant("Name 2", "rating 2", "price 2", R.drawable.envelope))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodlistBinding.inflate(layoutInflater)
        layoutManager = LinearLayoutManager(activity)
        val recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)

        binding.recyclerView.adapter = recyclerAdapter
        binding.recyclerView.layoutManager = layoutManager
        return binding.root
    }

}