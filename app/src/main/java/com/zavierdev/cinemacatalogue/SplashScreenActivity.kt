package com.zavierdev.cinemacatalogue

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.zavierdev.cinemacatalogue.ui.home.HomeActivity

class SplashScreenActivity : AppCompatActivity() {
    private val DELAY_ACTIVITY_SEC = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Disable dark theme

        val delayMilis: Int = DELAY_ACTIVITY_SEC * 1000
        Handler(mainLooper).postDelayed({
            startMainActivity()
        }, delayMilis.toLong())
    }

    private fun startMainActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}