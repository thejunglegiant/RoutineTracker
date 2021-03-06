package com.oleksii.routinetracker.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.oleksii.routinetracker.MainActivity
import com.oleksii.routinetracker.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        AnimationUtils.loadAnimation(this, R.anim.app_name_animation).also {
            findViewById<TextView>(R.id.app_name_text).startAnimation(it)
        }
        AnimationUtils.loadAnimation(this, R.anim.logo_animation).also {
            findViewById<ImageView>(R.id.logo).startAnimation(it)
        }

        val intent = Intent(this, MainActivity::class.java)

        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME)
    }
}
