package com.example.jude

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.jude.databinding.ActivityOnBoarding3Binding
import com.example.jude.databinding.ActivityMainBinding

class OnBoarding3 : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoarding3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportActionBar?.hide()


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding = ActivityOnBoarding3Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val backgroundImage: ImageView = binding.imgonboarding3


        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)


        binding.textNext3.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }


        binding.textBack3.setOnClickListener {
            val intent = Intent(this, OnBoarding2::class.java)
            startActivity(intent)
        }
    }
}
