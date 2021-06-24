package com.example.foodbunny.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.foodbunny.R

import com.example.foodbunny.databinding.ActivityFoodBinding
import com.example.foodbunny.fragment.FaqFragment
import com.example.foodbunny.fragment.FavoriteFragment
import com.example.foodbunny.fragment.FoodlistFragment
import com.example.foodbunny.fragment.ProfileFragment

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
}