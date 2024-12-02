package com.example.android.firstresponse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.animation.AnimationUtils
import android.widget.ImageButton


class DisasterManagement : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disaster_management)

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

        //switching to earthquake activity
        val buttonearth=findViewById<Button>(R.id.buttonEarthquake)
        buttonearth.setOnClickListener{
            val Intent= Intent(this,Earthquake::class.java)
            startActivity(Intent)
        }

        //switching to epidemic activity
        val buttonepidemic=findViewById<Button>(R.id.buttonEpidemic)
        buttonepidemic.setOnClickListener{
            val Intent= Intent(this,Epidemic::class.java)
            startActivity(Intent)
        }

        //switching to floods activity
        val buttonfloods=findViewById<Button>(R.id.buttonFloods)
        buttonfloods.setOnClickListener{
            val Intent= Intent(this,Floods::class.java)
            startActivity(Intent)
        }
        //switching to forest fires activity
        val buttonff=findViewById<Button>(R.id.buttonForestfire)
        buttonff.setOnClickListener{
            val Intent= Intent(this,VolcanicEruption::class.java)
            startActivity(Intent)
        }
    }

}