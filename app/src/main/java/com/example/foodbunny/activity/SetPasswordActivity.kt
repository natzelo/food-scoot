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
import com.example.foodbunny.databinding.ActivitySetPasswordBinding
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject

class SetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySetPasswordBinding.inflate(layoutInflater)
        val bundle: Bundle? = intent.extras

        binding.setPasswordBtn.setOnClickListener {

            val password: String = binding.setPassword.text.toString()
            val confirmPassword: String = binding.setPasswordConfirm.text.toString()

            if(password == confirmPassword) {

                val jsonObject = JSONObject()
                if (bundle != null) {
                    jsonObject.put("mobile_number", bundle.getString("mobile_number"))
                    jsonObject.put("otp", binding.setPasswordOtp.text.toString())
                    jsonObject.put("password", password)
                }

                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener {
                        try {
                            Log.i("DEBUG", "main hu $it")
                            val data = it.getJSONObject("data")
                            if(data.getBoolean("success")) {

                                val intent = Intent(this@SetPasswordActivity, LoginActivity::class.java)
                                Toast.makeText(this@SetPasswordActivity, "Password Reset Successfully", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@SetPasswordActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                            }


                        }    catch (e: JSONException) {
                            Toast.makeText(this@SetPasswordActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()

                        }
                }, Response.ErrorListener {
                    Toast.makeText(this@SetPasswordActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "5fc57ce44a41aa"
                        return headers
                    }
                }
                val queue = Volley.newRequestQueue(this@SetPasswordActivity)
                queue.add(jsonObjectRequest)




            } else {
                Toast.makeText(this@SetPasswordActivity, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }



        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}