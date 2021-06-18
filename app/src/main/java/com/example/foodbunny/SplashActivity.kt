package com.example.foodbunny

import android.content.Intent
import java.util.concurrent.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class SplashActivity : AppCompatActivity() {

    private fun toLogin() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        // Handler is deprecated
        //Handler().postDelayed({toLogin()}, 2000)

        val service = Executors.newSingleThreadScheduledExecutor()
        val runnable = Runnable { toLogin() }
        service.schedule(runnable, 2, TimeUnit.SECONDS)

    }
}