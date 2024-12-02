package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView

class SplashScreenActivity : BaseActivity() {
    private val pauseProgress: Float = 0.9f // Pause the animation at 90% completion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        supportActionBar?.hide()

        // Initialize the LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView.apply {
            setAnimation(R.raw.start)
            playAnimation()
            speed = 1.0f // Normal speed

            // Set up a listener to pause the animation at a specific progress
            addAnimatorUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                if (progress >= pauseProgress) {
                    pauseAnimation()
                    // Show the start button after the animation is complete
                    post {
                        showStartButton()
                    }
                }
            }
        }

        // Initialize the start button
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.alpha = 0f
        startButton.isEnabled = false

        // Load animations
        val buttonPressAnimation = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val buttonReleaseAnimation = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Add button click listener
        startButton.setOnTouchListener { _, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    startButton.startAnimation(buttonPressAnimation)
                }
                android.view.MotionEvent.ACTION_UP -> {
                    startButton.startAnimation(buttonReleaseAnimation)
                    // Start the MainActivity
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
    }

    private fun showStartButton() {
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.alpha = 1f
        startButton.isEnabled = true
    }
}
