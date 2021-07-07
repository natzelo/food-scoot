package com.example.foodbunny.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.room.Room
import com.example.foodbunny.R
import com.example.foodbunny.adaptor.RestaurantMenuRecyclerAdapter
import com.example.foodbunny.database.DBAsyncTask
import com.example.foodbunny.database.OrderEntity
import com.example.foodbunny.database.RestaurantDatabase
import com.example.foodbunny.database.RestaurantEntity

import com.example.foodbunny.databinding.ActivityFoodBinding
import com.example.foodbunny.fragment.*
import com.example.foodbunny.util.ConnectionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        setContentView(binding.root)
        setUpToolbar()
        openFoodlistFragment()


//    Set up an hamburger icon  which toggles the drawer
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, binding.drawer, R.string.open_drawer, R.string.close_drawer)

//        Pass the icon to the drawer listener so that it can change shape on events
        binding.drawer.addDrawerListener(actionBarDrawerToggle)

//      Sync the icon with the state of the DrawerLayout
        actionBarDrawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    openFoodlistFragment()
                    supportActionBar?.title = "Home"
                    binding.drawer.closeDrawers()
                }
                R.id.fav -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FavoriteFragment())
                        .commit()
                    supportActionBar?.title = "Favorites"
                    binding.drawer.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, ProfileFragment())
                        .commit()
                    supportActionBar?.title = "Profile"
                    binding.drawer.closeDrawers()
                }
                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, FaqFragment())
                        .commit()
                    supportActionBar?.title = "FAQ"
                    binding.drawer.closeDrawers()
                }
                R.id.order_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, OrderHistoryFragment()
                        ).commit()
                    supportActionBar?.title = "Order History"
                    binding.drawer.closeDrawers()
                }

                R.id.log_out -> {


                        val dialog = AlertDialog.Builder(this@MainActivity)
                        dialog.setTitle("Log out?")
                        dialog.setMessage("Your favorites restaurant list will be removed")
                        dialog.setPositiveButton("Log Out"){ _, _ ->
                            sharedPreferences.edit().clear().apply()
                            RestaurantMenuRecyclerAdapter.OrderDBAsyncTask(this@MainActivity, OrderEntity("dummy", "dummy"), 2 ).execute().get()
                            DeleteFavRestaurants(this@MainActivity).execute().get()
                            finishAffinity()
                        }
                        dialog.setNegativeButton("Cancel") { _, _ ->

                        }

                        dialog.create()
                        dialog.show()

                }

            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Home"

        //Allows the home button to be used
        supportActionBar?.setHomeButtonEnabled(true)

        // Displays the home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    // open the drawer when the the home button of the action bar is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id == android.R.id.home) {
            binding.drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFoodlistFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, FoodlistFragment())
            .commit()
        supportActionBar?.title = "Home"
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frame_layout)) {
            !is FoodlistFragment -> {
                openFoodlistFragment()
            }
            else-> {
                finishAffinity()
                super.onBackPressed()
            }
        }
    }

    class DeleteFavRestaurants(val context: Context): AsyncTask<Void, Void,Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db").build()
            db.restaurantDao().nukeTable()
            db.close()
            return true
        }

    }
}