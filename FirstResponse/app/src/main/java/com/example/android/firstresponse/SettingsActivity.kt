package com.example.android.firstresponse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var cardUSA: CardView
    private lateinit var cardPhilippines: CardView
    private lateinit var imageUSA: ImageView
    private lateinit var imagePhilippines: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale() // Apply locale before setting content view
        setContentView(R.layout.activity_settings)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = getString(R.string.language_t)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.red))

        // Show back button on the Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)

        // Set up back button click listener with animation
        toolbar.setNavigationOnClickListener {
            it.startAnimation(pressAnim)
            it.postDelayed({
                it.startAnimation(releaseAnim)
                finish()
            }, pressAnim.duration)
        }

        cardUSA = findViewById(R.id.cardUSA)
        cardPhilippines = findViewById(R.id.cardPhilippines)
        imageUSA = findViewById(R.id.imageUSA)
        imagePhilippines = findViewById(R.id.imagePhilippines)

        // Load the saved language and set the card backgrounds
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"

        if (savedLanguage == "en") {
            cardUSA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.shadow3))
            cardPhilippines.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        } else {
            cardUSA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            cardPhilippines.setCardBackgroundColor(ContextCompat.getColor(this, R.color.shadow3))
        }

        cardUSA.setOnClickListener {
            setLocale("en")
            updateCardSelection(cardUSA, cardPhilippines)
        }

        cardPhilippines.setOnClickListener {
            setLocale("fil")
            updateCardSelection(cardPhilippines, cardUSA)
        }
    }

    private fun setLocale(languageCode: String) {
        // Save the selected language to SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("selected_language", languageCode)
            apply()
        }

        // Log the selected language
        Log.d("SettingsActivity", "Selected Language: $languageCode")

        // Send a broadcast to notify other activities
        val intent = Intent("LANGUAGE_CHANGED")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        // Recreate the activity to apply the changes
        recreate()
    }

    private fun updateCardSelection(selectedCard: CardView, unselectedCard: CardView) {
        selectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.shadow3))
        unselectedCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun applyLocale() {
        // Retrieve the saved language from SharedPreferences and set it
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"

        val locale = Locale(savedLanguage)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
