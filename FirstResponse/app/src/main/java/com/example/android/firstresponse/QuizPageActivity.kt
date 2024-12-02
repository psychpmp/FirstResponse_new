package com.example.android.firstresponse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuizPageActivity : BaseActivity() {
    private lateinit var buttonLevel1: Button
    private lateinit var buttonLevel2: Button
    private lateinit var buttonLevel3: Button
    private lateinit var lockIconLevel2: ImageView
    private lateinit var lockIconLevel3: ImageView
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_page)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Set up the back button in the action bar
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            // Start the press animation
            backButton.startAnimation(pressAnim)

            // Delayed action to finish activity and apply release animation
            backButton.postDelayed({
                // Apply release animation (optional)
                backButton.startAnimation(releaseAnim)

                // Close the activity
                finish()
            }, pressAnim.duration)
        }


        buttonLevel1 = findViewById(R.id.buttonLevel1)
        buttonLevel2 = findViewById(R.id.buttonLevel2)
        buttonLevel3 = findViewById(R.id.buttonLevel3)
        lockIconLevel2 = findViewById(R.id.lockIconLevel2)
        lockIconLevel3 = findViewById(R.id.lockIconLevel3)

        updateButtonStates()

        buttonLevel1.setOnClickListener {
            startLevel1Quiz()
        }

        buttonLevel2.setOnClickListener {
            if (sharedPreferences.getBoolean("Level2Unlocked", false)) {
                startLevel2Quiz()
            } else {
                Toast.makeText(this, "Complete LVL 1 with a perfect score to unlock LVL 2", Toast.LENGTH_SHORT).show()
            }
        }

        buttonLevel3.setOnClickListener {
            if (sharedPreferences.getBoolean("Level3Unlocked", false)) {
                startLevel3Quiz()
            } else {
                Toast.makeText(this, "Complete LVL 2 with a perfect score to unlock LVL 3", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLevel1Quiz() {
        val intent = Intent(this, QuizLevel1Activity::class.java)
        startActivityForResult(intent, 1)
    }

    private fun startLevel2Quiz() {
        val intent = Intent(this, QuizLevel2Activity::class.java)
        startActivityForResult(intent, 2)
    }

    private fun startLevel3Quiz() {
        val intent = Intent(this, QuizLevel3Activity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val isPerfectScore = data?.getBooleanExtra("isPerfectScore", false) ?: false
            when (requestCode) {
                1 -> {
                    if (isPerfectScore) {
                        sharedPreferences.edit().putBoolean("Level2Unlocked", true).apply()
                        updateButtonStates()
                    }
                }
                2 -> {
                    if (isPerfectScore) {
                        sharedPreferences.edit().putBoolean("Level3Unlocked", true).apply()
                        updateButtonStates()
                    }
                }
            }
        }
    }

    private fun updateButtonStates() {
        buttonLevel2.isEnabled = sharedPreferences.getBoolean("Level2Unlocked", false)
        buttonLevel3.isEnabled = sharedPreferences.getBoolean("Level3Unlocked", false)

        lockIconLevel2.visibility = if (buttonLevel2.isEnabled) View.GONE else View.VISIBLE
        lockIconLevel3.visibility = if (buttonLevel3.isEnabled) View.GONE else View.VISIBLE

        buttonLevel2.setBackgroundTintList(
            ContextCompat.getColorStateList(this, if (buttonLevel2.isEnabled) R.color.shadow2 else R.color.shadow4)
        )
        buttonLevel3.setBackgroundTintList(
            ContextCompat.getColorStateList(this, if (buttonLevel3.isEnabled) R.color.shadow2 else R.color.shadow4)
        )
    }
}