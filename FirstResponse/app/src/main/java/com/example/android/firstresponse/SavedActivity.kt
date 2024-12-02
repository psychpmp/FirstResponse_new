package com.example.android.firstresponse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SavedActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyAnimation: LottieAnimationView
    private lateinit var noSavedTopicsText: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var sharedPreferences: SharedPreferences
    private val savedTopics = mutableListOf<SavedTopic>()
    private lateinit var adapter: SavedTopicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        // Hide the Action Bar
        supportActionBar?.hide()

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        emptyAnimation = findViewById(R.id.empty_animation)
        noSavedTopicsText = findViewById(R.id.no_saved_topics_text)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SavedTopics", Context.MODE_PRIVATE)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SavedTopicsAdapter(savedTopics) { topicId ->
            navigateToTopic(topicId)
        }
        recyclerView.adapter = adapter

        // Load saved topics from SharedPreferences
        loadSavedTopics()

        // Bottom Navigation setup
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottomHome -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.bottomHelpline -> {
                    startActivity(Intent(this, HelplineActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.bottomSaved -> {
                    // Already in SavedActivity, no need to navigate
                    true
                }
                else -> false
            }
        }

    }

    private fun loadSavedTopics() {
        savedTopics.clear()

        // Load saved topics from SharedPreferences as a JSON string
        val savedTopicsJson = sharedPreferences.getString("savedTopicsJson", "[]")
        val gson = Gson()
        val type = object : TypeToken<List<SavedTopic>>() {}.type
        val savedTopicList: List<SavedTopic> = gson.fromJson(savedTopicsJson, type)

        savedTopics.addAll(savedTopicList)
        adapter.notifyDataSetChanged()

        // Update UI based on whether there are saved topics
        if (savedTopics.isEmpty()) {
            recyclerView.visibility = RecyclerView.GONE
            emptyAnimation.visibility = LottieAnimationView.VISIBLE
            emptyAnimation.playAnimation() // Ensure this line is executed
            noSavedTopicsText.visibility = TextView.VISIBLE
        } else {
            recyclerView.visibility = RecyclerView.VISIBLE
            emptyAnimation.visibility = LottieAnimationView.GONE
            noSavedTopicsText.visibility = TextView.GONE
        }
    }


        private fun navigateToTopic(topicId: String) {
        // Define a map of topic IDs to their corresponding activity classes
        val activityMap = mapOf(
            "burns" to burns::class.java,
            "choking" to choking::class.java,
            "cpr" to CPR::class.java,
            "splints" to splints::class.java,
            "seizures" to seizures::class.java,
            "shock" to shock::class.java,
            "bleeding" to bleeding::class.java,
            "snakebite" to snakebite::class.java,
            "insectbite" to insectbite::class.java,
            "bruises" to bruises::class.java,
            "sprain" to sprain::class.java,
            "strain" to strain::class.java,
            "nosebleed" to nosebleed::class.java,
            "allergic reaction" to allergicreaction::class.java,
            "headache" to headache::class.java,
            "minor_concussion" to minorconcussion::class.java,
            "muscle_cramps" to musclecramps::class.java,
            "blister" to blister::class.java,
            "anxiety management" to AnxietyManagement::class.java,
            "panic attack response" to PanicAttackResponse::class.java,
            "trauma-informed care" to TraumaInformedCare::class.java,
            "grounding techniques" to GroundingTechniques::class.java,
            "stress_reduction" to StressReduction::class.java,
            "floods" to Floods::class.java,
            "acute grief" to AcuteGrief::class.java,
            "volcanic eruption" to VolcanicEruption::class.java,
            "epidemic" to Epidemic::class.java,
            "earthquake" to Earthquake::class.java,
            // New additions
            "water safety" to WaterSafety::class.java,
            "road safety" to RoadSafety::class.java,
            "daily food safety" to DailyFoodSafety::class.java,
            "emergency food safety" to EmergencyFoodSafety::class.java,
            "heatwave" to Heatwave::class.java
        )

        // Navigate to the corresponding activity
        val activityClass = activityMap[topicId]
        if (activityClass != null) {
            val intent = Intent(this, activityClass)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}

