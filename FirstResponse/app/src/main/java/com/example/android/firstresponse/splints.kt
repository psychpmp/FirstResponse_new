package com.example.android.firstresponse

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class splints : BaseActivity() {

    private lateinit var webView1: WebView
    private lateinit var webView2: WebView
    private lateinit var fabSave: FloatingActionButton
    private val topicId = "splints" // Unique ID for the topic
    private val topicTitle = "Splints" // Topic title for display

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splints)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = getString(R.string.splints_t)
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

    // Initialize WebViews
        webView1 = findViewById(R.id.webView)
        webView1.settings.javaScriptEnabled = true
        webView1.webViewClient = WebViewClient()
        val videoId1 = "2v8vlXgGXwE"
        val videoUrl1 = "https://www.youtube.com/embed/$videoId1"
        webView1.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl1\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8")

        webView2 = findViewById(R.id.webView2)
        webView2.settings.javaScriptEnabled = true
        webView2.webViewClient = WebViewClient()
        val videoId2 = "s6PZj2mGqbs"
        val videoUrl2 = "https://www.youtube.com/embed/$videoId2"
        webView2.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl2\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8")

        // Initialize FloatingActionButton for saving the topic
        fabSave = findViewById(R.id.fab_save)
        updateFabIcon()

        fabSave.setOnClickListener {
            toggleSaveTopic()
        }
    }

    // Function to toggle save status
    private fun toggleSaveTopic() {
        val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
        val gson = Gson()

        // Load saved topics from SharedPreferences
        val savedTopicsJson = sharedPref.getString("savedTopicsJson", "[]")
        val type = object : TypeToken<MutableList<SavedTopic>>() {}.type
        val savedTopicsSet: MutableList<SavedTopic> = gson.fromJson(savedTopicsJson, type)

        // Check if the topic is already saved
        val isSaved = savedTopicsSet.any { it.id == topicId }

        if (isSaved) {
            // Remove the topic if it's already saved
            savedTopicsSet.removeAll { it.id == topicId }
            Toast.makeText(this, "Topic removed", Toast.LENGTH_SHORT).show()
        } else {
            // Add the topic if it's not saved
            savedTopicsSet.add(SavedTopic(topicId, topicTitle))
            Toast.makeText(this, "Topic saved", Toast.LENGTH_SHORT).show()
        }

        // Save the updated list of topics to SharedPreferences
        val updatedTopicsJson = gson.toJson(savedTopicsSet)
        with(sharedPref.edit()) {
            putString("savedTopicsJson", updatedTopicsJson)
            apply()
        }
        updateFabIcon()
    }

    // Function to update FloatingActionButton icon based on save state
    private fun updateFabIcon() {
        val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
        val gson = Gson()

        // Load saved topics from SharedPreferences
        val savedTopicsJson = sharedPref.getString("savedTopicsJson", "[]")
        val type = object : TypeToken<List<SavedTopic>>() {}.type
        val savedTopicsSet: List<SavedTopic> = gson.fromJson(savedTopicsJson, type)

        // Check if the topic is already saved
        val isSaved = savedTopicsSet.any { it.id == topicId }
        val iconResId = if (isSaved) R.drawable.saved_red else R.drawable.saved
        fabSave.setImageDrawable(ContextCompat.getDrawable(this, iconResId))
    }

    // Function to handle the back button in the action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (webView1.canGoBack()) {
            webView1.goBack()
        } else if (webView2.canGoBack()) {
            webView2.goBack()
        } else {
            super.onBackPressed()
        }
    }
}