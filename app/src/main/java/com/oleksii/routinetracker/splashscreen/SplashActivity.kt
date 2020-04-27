package com.oleksii.routinetracker.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.oleksii.routinetracker.MainActivity
import com.oleksii.routinetracker.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val intent = Intent(this, MainActivity::class.java)

        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME)
    }
}
