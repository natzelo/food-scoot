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
import com.example.foodbunny.databinding.ActivityRegisterBinding
import org.json.JSONException
import org.json.JSONObject


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        binding.registerButton.setOnClickListener {
            when(validate()) {
                0 -> {
                    sendRequest()
                }

                1 -> {
                    Toast.makeText(this@RegisterActivity, "Cannot leave blank fields", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    Toast.makeText(this@RegisterActivity, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
                }

                3-> {
                    Toast.makeText(this@RegisterActivity, "Passwords don't match", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setContentView(binding.root)
    }

    private fun validate():Int {

        if(binding.registerName.text.toString() == ""
            || binding.registerAddress.text.toString() == ""
            || binding.registerEmail.text.toString() == ""
            || binding.registerPassword.text.toString() == ""
            || binding.registerCfmPassword.text.toString() == ""
        ) {
            return 1
        }

        if (binding.registerPhone.text.toString().length != 10) {
            return 2
        }

        if(binding.registerPassword.text.toString() != binding.registerCfmPassword.text.toString()) {
            return 3
        }

        return 0

    }

    private fun sendRequest(){
        val url = "http://13.235.250.119/v2/register/fetch_result"
        val jsonObject = JSONObject()

        jsonObject.put("name", binding.registerName.text.toString())
        jsonObject.put("mobile_number", binding.registerPhone.text.toString())
        jsonObject.put("password", binding.registerPassword.text.toString())
        jsonObject.put("email", binding.registerEmail.text.toString())
        jsonObject.put("address", binding.registerAddress.text.toString())

        val queue = Volley.newRequestQueue(this@RegisterActivity)

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener {
              try {
                  val data = it.getJSONObject("data")
                  val success = data.getBoolean("success")

                  if (success) {
                      Toast.makeText(this@RegisterActivity, "Account Successfully Made!", Toast.LENGTH_SHORT ).show()
                      val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                      startActivity(intent)
                      finish()
                  }
              }  catch (e: JSONException) {
                  Toast.makeText(this@RegisterActivity, "Some Error Occurred", Toast.LENGTH_SHORT ).show()

              }
        }, Response.ErrorListener {
            Toast.makeText(this@RegisterActivity, "Some Error Occurred", Toast.LENGTH_SHORT ).show()
        } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["token"] = "5fc57ce44a41aa"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
    }

}