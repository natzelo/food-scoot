package com.example.foodbunny.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodbunny.R
import com.example.foodbunny.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private  lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        if(activity != null) {
            sharedPreferences =
                activity?.getSharedPreferences( getString(R.string.shared_preferences), Context.MODE_PRIVATE )!!

            binding.personName.text = sharedPreferences.getString("name", "")
            binding.personNumber.text = sharedPreferences.getString("mobile_number", "")
            binding.personEmail.text = sharedPreferences.getString("email", "")
            binding.personLocation.text = sharedPreferences.getString("address", "")


        }

        return binding.root
    }


}