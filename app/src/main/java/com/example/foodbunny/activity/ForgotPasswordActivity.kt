package com.example.foodbunny.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodbunny.R
import com.example.foodbunny.databinding.ActivityForgotPasswordBinding
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        binding.nextButton.setOnClickListener {

            val jsonObject = JSONObject()
            jsonObject.put("mobile_number", binding.forgotPasswordPhone.text.toString())
            jsonObject.put("email", binding.forgotPasswordEmail.text.toString())

            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

            val jsonObjectRequest: JsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener {
                Log.i("DEBUG", " main hu $it")
                val data = it.getJSONObject("data")
                if(data.getBoolean("success")) {
                        val bundle = Bundle()
                        bundle.putString("mobile_number", binding.forgotPasswordPhone.text.toString())
                        val intent = Intent(this@ForgotPasswordActivity, SetPasswordActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)

                } else {

                    Toast.makeText(this@ForgotPasswordActivity, "Some Error Occurred", Toast.LENGTH_SHORT).show()

                }
            }, Response.ErrorListener {

                Toast.makeText(this@ForgotPasswordActivity, "Soe Error Occurred", Toast.LENGTH_SHORT).show()

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "5fc57ce44a41aa"
                    return headers
                }
            }

            val queue =  Volley.newRequestQueue(this@ForgotPasswordActivity)
            queue.add(jsonObjectRequest)

        }

        setContentView(binding.root)
    }
}