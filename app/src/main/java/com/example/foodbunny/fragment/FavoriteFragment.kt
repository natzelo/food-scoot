package com.example.foodbunny.fragment


import android.content.Context

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.foodbunny.adaptor.FoodlistRecyclerAdapter
import com.example.foodbunny.database.RestaurantDatabase
import com.example.foodbunny.database.RestaurantEntity
import com.example.foodbunny.databinding.FragmentFoodlistBinding
import com.example.foodbunny.model.Restaurant
import java.util.ArrayList


class FavoriteFragment : Fragment() {

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


        layoutManager = LinearLayoutManager(activity)


        val restaurantEntityList = DBAsyncTaskForFav(activity as Context).execute().get()

        for(i in restaurantEntityList.indices) {
            val restaurantEntity: RestaurantEntity = restaurantEntityList[i]

            val restaurant = Restaurant(
                restaurantEntity.restaurantId,
                restaurantEntity.name,
                restaurantEntity.rating,
                restaurantEntity.price,
                restaurantEntity.dish
            )

            itemList.add(restaurant)
        }

        val recyclerAdapter = FoodlistRecyclerAdapter(activity as Context, itemList)

        binding.recyclerView.adapter = recyclerAdapter
        binding.recyclerView.layoutManager = layoutManager
        binding.progressLayout.visibility = View.GONE
        return binding.root
    }


    class DBAsyncTaskForFav(val context: Context): AsyncTask<Void, Void, List<RestaurantEntity> >() {

        private val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db" ).build()
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {

            val result = db.restaurantDao().getAllRestaurants()
            db.close()
            return result
        }

    }

}