package com.example.jude

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.jude.databinding.ActivityOnBoarding1Binding

class OnBoarding1 : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoarding1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportActionBar?.hide()


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding = ActivityOnBoarding1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val backgroundImage: ImageView = binding.imgonboarding1


        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)


        binding.textNext.setOnClickListener {
            val intent = Intent(this, OnBoarding2::class.java)
            startActivity(intent)
        }
    }
}
