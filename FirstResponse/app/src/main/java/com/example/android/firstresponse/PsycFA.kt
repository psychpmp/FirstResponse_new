package com.example.android.firstresponse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.content.ContextCompat


class PsycFA : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psycfa)
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

        // Switching to Acute Grief activity
        val buttonAcuteGrief = findViewById<Button>(R.id.buttonAcuteGrief)
        buttonAcuteGrief.setOnClickListener {
            val intent = Intent(this, AcuteGrief::class.java)
            startActivity(intent)
        }

        // Switching to Anxiety Management activity
        val buttonAnxiety = findViewById<Button>(R.id.buttonAnxiety)
        buttonAnxiety.setOnClickListener {
            val intent = Intent(this, AnxietyManagement::class.java)
            startActivity(intent)
        }

        // Switching to Panic Attack Response activity
        val buttonPanic = findViewById<Button>(R.id.buttonPanic)
        buttonPanic.setOnClickListener {
            val intent = Intent(this, PanicAttackResponse::class.java)
            startActivity(intent)
        }

        // Switching to Trauma-Informed Care activity
        val buttonTraumaCare = findViewById<Button>(R.id.buttonTraumaCare)
        buttonTraumaCare.setOnClickListener {
            val intent = Intent(this, TraumaInformedCare::class.java)
            startActivity(intent)
        }

        // Switching to Grounding Techniques activity
        val buttonGrounding = findViewById<Button>(R.id.buttonGrounding)
        buttonGrounding.setOnClickListener {
            val intent = Intent(this, GroundingTechniques::class.java)
            startActivity(intent)
        }

        // Switching to Stress Reduction activity
        val buttonStressReduction = findViewById<Button>(R.id.buttonStressReduction)
        buttonStressReduction.setOnClickListener {
            val intent = Intent(this, StressReduction::class.java)
            startActivity(intent)
        }
    }
}
