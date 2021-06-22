package com.example.foodbunny.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodbunny.R
import com.example.foodbunny.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private fun toRegister() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun toForgotPassword() {
        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun handleLogin() {
        val phone: String = binding.editTextPhone.text.toString()
        val password: String = binding.editTextPasswordLogin.text.toString()

        if(phone == "8962151380" && password == "qwerty") {
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
            sharedPreferences.edit().putString("phone", phone).apply()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            Toast.makeText(applicationContext, "Invalid", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUptextView.setOnClickListener { toRegister() }
        binding.forgotPasswordTextView.setOnClickListener { toForgotPassword() }
        binding.logInButton.setOnClickListener { handleLogin() }

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

        val isLoggedIn:Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

        if(isLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}