package com.example.homemanagement

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class StartPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)

        // Reference the TextView
        val titleTextView: TextView = findViewById(R.id.titleTextView)

        // Load the fade-in animation
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Apply the animation to the TextView
        titleTextView.startAnimation(fadeIn)

        // Delay for 3 seconds before starting LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 milliseconds = 3 seconds
    }
}
