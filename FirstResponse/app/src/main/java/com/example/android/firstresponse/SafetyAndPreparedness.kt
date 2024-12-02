package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem // Import MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SafetyAndPreparedness : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_and_preparedness)

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

        // Switching to Water Safety activity
        val buttonWater = findViewById<Button>(R.id.buttonWater)
        buttonWater.setOnClickListener {
            val intent = Intent(this, WaterSafety::class.java)
            startActivity(intent)
        }

        // Switching to Road Safety activity
        val buttonRoad = findViewById<Button>(R.id.buttonRoad)
        buttonRoad.setOnClickListener {
            val intent = Intent(this, RoadSafety::class.java)
            startActivity(intent)
        }

        // Switching to Daily Food Safety activity
        val buttonDailyFood = findViewById<Button>(R.id.buttonDailyFood)
        buttonDailyFood.setOnClickListener {
            val intent = Intent(this, DailyFoodSafety::class.java)
            startActivity(intent)
        }

        // Switching to Emergency Food Safety activity
        val buttonEmergency = findViewById<Button>(R.id.buttonEmergency)
        buttonEmergency.setOnClickListener {
            val intent = Intent(this, EmergencyFoodSafety::class.java)
            startActivity(intent)
        }

        // Switching to Heatwave activity
        val buttonHeatwave = findViewById<Button>(R.id.buttonHeatwave)
        buttonHeatwave.setOnClickListener {
            val intent = Intent(this, Heatwave::class.java)
            startActivity(intent)
        }
    }

    // Handle Up button presses
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Finish the current activity and go back to the previous one
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
