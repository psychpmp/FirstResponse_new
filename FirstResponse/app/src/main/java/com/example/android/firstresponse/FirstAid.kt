package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class FirstAid : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid)

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

        // Set up button click listeners for switching to different activities

        //switching to cpr activity
        val buttonCPR=findViewById<Button>(R.id.buttonCPR)
        buttonCPR.setOnClickListener{
            val Intent= Intent(this,CPR::class.java)
            startActivity(Intent)
        }

        //switching to shock activity
        val buttonShock=findViewById<Button>(R.id.buttonShock)
        buttonShock.setOnClickListener{
            val Intent= Intent(this,shock::class.java)
            startActivity(Intent)
        }

        //switching to splints activity
        val buttonSplint=findViewById<Button>(R.id.buttonSplints)
        buttonSplint.setOnClickListener{
            val Intent= Intent(this,splints::class.java)
            startActivity(Intent)
        }

        //switching to choking activity
        val buttonchoking=findViewById<Button>(R.id.buttonChoking)
        buttonchoking.setOnClickListener{
            val Intent= Intent(this,choking::class.java)
            startActivity(Intent)
        }

        //switching to burns activity
        val buttonburns=findViewById<Button>(R.id.buttonBurns)
        buttonburns.setOnClickListener{
            val Intent= Intent(this,burns::class.java)
            startActivity(Intent)
        }

        //switching to seizures activity
        val buttonseizures=findViewById<Button>(R.id.buttonSeizures)
        buttonseizures.setOnClickListener{
            val Intent= Intent(this,seizures::class.java)
            startActivity(Intent)
        }

        val buttonbleeding=findViewById<Button>(R.id.buttonBleeding)
        buttonbleeding.setOnClickListener{
            val Intent= Intent(this,bleeding::class.java)
            startActivity(Intent)
        }

        val buttonsnakebite=findViewById<Button>(R.id.buttonSnakeBite)
        buttonsnakebite.setOnClickListener {
            val Intent = Intent(this, snakebite::class.java)
            startActivity(Intent)
        }

        val buttonsprain=findViewById<Button>(R.id.buttonSprains)
        buttonsprain.setOnClickListener {
            val Intent = Intent(this, sprain::class.java)
            startActivity(Intent)
        }

        val buttonnosebleed=findViewById<Button>(R.id.buttonNosebleeds)
        buttonnosebleed.setOnClickListener {
            val Intent = Intent(this, nosebleed::class.java)
            startActivity(Intent)
        }

        val buttonminorconcussion=findViewById<Button>(R.id.buttonMinorConcussions)
        buttonminorconcussion.setOnClickListener {
            val Intent = Intent(this, minorconcussion::class.java)
            startActivity(Intent)
        }

        val buttonheadache=findViewById<Button>(R.id.buttonHeadaches)
        buttonheadache.setOnClickListener {
            val Intent = Intent(this, headache::class.java)
            startActivity(Intent)
        }

        val buttonmusclecramps=findViewById<Button>(R.id.buttonMuscleCramps)
        buttonmusclecramps.setOnClickListener {
            val Intent = Intent(this, musclecramps::class.java)
            startActivity(Intent)
        }

        val buttonblister=findViewById<Button>(R.id.buttonBlisters)
        buttonblister.setOnClickListener {
            val Intent = Intent(this, blister::class.java)
            startActivity(Intent)
        }

        val buttonbruises=findViewById<Button>(R.id.buttonBruises)
        buttonbruises.setOnClickListener {
            val Intent = Intent(this, bruises::class.java)
            startActivity(Intent)
        }

        val buttonallergicreaction=findViewById<Button>(R.id.buttonAllergicReactions)
        buttonallergicreaction.setOnClickListener {
            val Intent = Intent(this, allergicreaction::class.java)
            startActivity(Intent)
        }

        val buttonstrain=findViewById<Button>(R.id.buttonStrains)
        buttonstrain.setOnClickListener {
            val Intent = Intent(this, strain::class.java)
            startActivity(Intent)
        }

        val buttoninsectbites=findViewById<Button>(R.id.buttonInsectBites)
        buttoninsectbites.setOnClickListener {
            val Intent = Intent(this, insectbite::class.java)
            startActivity(Intent)
        }

    }

}