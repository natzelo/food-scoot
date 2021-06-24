package com.example.foodbunny.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.R
import com.example.foodbunny.databinding.ActivityLoginBinding
import org.json.JSONObject

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
        val jsonRequest = JSONObject()
        jsonRequest.put("mobile_number", binding.loginPhone.text.toString())
        jsonRequest.put("password", binding.loginPassword.text.toString())
        val url = "http://13.235.250.119/v2/login/fetch_result"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonRequest,
            Response.Listener {
                val data = it.getJSONObject("data")
                if (data.getBoolean("success")) {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    val person = data.getJSONObject("data")
                    editor.putString("name", person.getString("name"))
                    editor.putString("mobile_number", person.getString("mobile_number"))
                    editor.putString("address", person.getString("address"))
                    editor.putString("email", person.getString("email"))
                    editor.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },
            Response.ErrorListener { }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "5fc57ce44a41aa"
                return headers
            }

        }

        val queue = Volley.newRequestQueue(this@LoginActivity)
        queue.add(jsonObjectRequest)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.signUptextView.setOnClickListener { toRegister() }
            binding.forgotPasswordTextView.setOnClickListener { toForgotPassword() }
            binding.logInButton.setOnClickListener { handleLogin() }

            sharedPreferences =
                getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE)

            val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }



}