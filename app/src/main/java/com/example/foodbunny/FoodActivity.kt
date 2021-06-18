package com.example.foodbunny

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import com.example.foodbunny.databinding.ActivityFoodBinding

class FoodActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityTrashBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityTrashBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        setContentView(R.layout.activity_trash)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)
        //binding.trash.text = sharedPreferences.getString("phone", "No phone")

    }
}