package com.ekc.devartlabproject.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ekc.devartlabproject.MainActivity
import com.ekc.devartlabproject.R
import com.ekc.devartlabproject.databinding.SplashScreenLayoutBinding

class SplashScreenFragment : AppCompatActivity() {


    private lateinit var binding: SplashScreenLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_screen_layout);
        init()
    }

    private fun init() {
        initAnimation()
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }

    private fun initAnimation() {
        val fadeIn: Animation = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() //add this

        fadeIn.duration = 3500


        val animation = AnimationSet(true) //change to false
        animation.addAnimation(fadeIn)
        animation.fillAfter = true

        binding.logo.animation = animation



    }
}