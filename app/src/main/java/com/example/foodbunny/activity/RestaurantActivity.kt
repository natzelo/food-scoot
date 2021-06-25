package com.example.foodbunny.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.RestaurantMenuRecyclerAdapter
import com.example.foodbunny.database.OrderEntity
import com.example.foodbunny.databinding.ActivityRestaurantBinding
import com.example.foodbunny.fragment.FragmentRestaurantMenu


class RestaurantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantBinding.inflate(layoutInflater)
        val restaurantId = intent.getStringExtra("restaurant_id")
        Log.i("ID TRACK", "$restaurantId activity")

        setUpToolbar()
        openMenu(restaurantId as String)
        setContentView(binding.root)
    }

    override fun onBackPressed() {

        RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(this@RestaurantActivity, OrderEntity("dummy", "dummy"), 2).execute()
        val intent = Intent(this@RestaurantActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        Log.i("DEBUG", "On Destroy Called")
        RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(this@RestaurantActivity, OrderEntity("dummy", "dummy"), 2).execute()
        super.onDestroy()
    }

    override fun onStop() {
        Log.i("DEBUG", "On Stop Called")
        //RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(this@RestaurantActivity, OrderEntity("dummy", "dummy"), 2).execute()
        super.onStop()
    }

    private fun openMenu(restaurantId: String){
        val bundle = Bundle()
        bundle.putString("restaurant_id",restaurantId)
        val restaurantMenuFragment = FragmentRestaurantMenu()
        restaurantMenuFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_restaurant_activity, restaurantMenuFragment).commit()
    }

    private fun setUpToolbar() {
        Log.i("Action bar track", "setUpToolbarCalled")
        setSupportActionBar(binding.toolbarRestaurantActivity)
        supportActionBar?.title = "Menu"
    }

}