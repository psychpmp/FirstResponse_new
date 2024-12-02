package com.example.android.firstresponse

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class SearchResultsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = "SEARCH RESULTS"  // Replace with your desired title
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


    val listView: ListView = findViewById(R.id.listView)
        val resultsLabel: TextView = findViewById(R.id.resultsLabel)

        // Get the search input and results from the Intent
        val searchResults = intent.getStringArrayExtra("searchResults") ?: arrayOf()
        val searchQuery = intent.getStringExtra("searchQuery") ?: "Unknown"

        // Set the search input in the results label
        resultsLabel.text = "Search results for $searchQuery"

        // Set up custom adapter
        val adapter = SearchResultsAdapter(this, searchResults)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = searchResults[position]
            val intent = when (selectedItem) {
                "burns" -> Intent(this, burns::class.java)
                "choking" -> Intent(this, choking::class.java)
                "cpr" -> Intent(this, CPR::class.java)
                "splints" -> Intent(this, splints::class.java)
                "seizures" -> Intent(this, seizures::class.java)
                "shock" -> Intent(this, shock::class.java)
                "bleeding" -> Intent(this, bleeding::class.java)
                "snakebite" -> Intent(this, snakebite::class.java)
                "bruises" -> Intent(this, bruises::class.java)
                "sprains" -> Intent(this, sprain::class.java)
                "strains" -> Intent(this, strain::class.java)
                "nosebleeds" -> Intent(this, nosebleed::class.java)
                "allergic reaction" -> Intent(this, allergicreaction::class.java)
                "headaches" -> Intent(this, headache::class.java)
                "minor concussions" -> Intent(this, minorconcussion::class.java)
                "muscle cramp" -> Intent(this, musclecramps::class.java)
                "blister" -> Intent(this, blister::class.java)
                "anxiety management" -> Intent(this, AnxietyManagement::class.java)
                "panic attack response" -> Intent(this, PanicAttackResponse::class.java)
                "trauma-informed care" -> Intent(this, TraumaInformedCare::class.java)
                "grounding techniques" -> Intent(this, GroundingTechniques::class.java)
                "stress reduction" -> Intent(this, StressReduction::class.java)
                "floods" -> Intent(this, Floods::class.java)
                "acute grief" -> Intent(this, AcuteGrief::class.java)
                "volcanic eruption" -> Intent(this, VolcanicEruption::class.java)
                "epidemic" -> Intent(this, Epidemic::class.java)
                "earthquake" -> Intent(this, Earthquake::class.java)
                // New additions
                "water safety" -> Intent(this, WaterSafety::class.java)
                "road safety" -> Intent(this, RoadSafety::class.java)
                "daily food safety" -> Intent(this, DailyFoodSafety::class.java)
                "emergency food safety" -> Intent(this, EmergencyFoodSafety::class.java)
                "heatwave" -> Intent(this, Heatwave::class.java)

                else -> null
            }

            if (intent != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No activity found for: $selectedItem", Toast.LENGTH_SHORT).show()
            }
        }
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
