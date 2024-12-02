package com.example.android.firstresponse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class QuizLevel2ResultActivity : BaseActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var homeButton: Button

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_level2_result)

        supportActionBar?.hide()

        // Initialize LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView.apply {
            setAnimation(R.raw.result)
            playAnimation()
            repeatCount = LottieDrawable.INFINITE
        }

        resultTextView = findViewById(R.id.resultTextView)
        retryButton = findViewById(R.id.retryButton)
        homeButton = findViewById(R.id.homeButton)

        val score = intent.getIntExtra("score", 0)
        val highestScore = sharedPreferences.getInt("highest_score_level2", 0)

        if (score > highestScore) {
            sharedPreferences.edit().putInt("highest_score_level2", score).apply()
        }

        if (score == 15) {
            sharedPreferences.edit().putBoolean("Level3Unlocked", true).apply()
        }

        val feedbackMessage = when {
            score == 15 -> "Excellent! Level 3 is now available!"
            score >= 10 -> "Great job! You have a good grasp of the material."
            score >= 6 -> "Good effort! Keep practicing to improve."
            else -> "Keep trying! You'll get better with more practice."
        }

        resultTextView.text = "Your Score: $score\nHighest Score: $highestScore\n$feedbackMessage"

        retryButton.setOnClickListener {
            finish() // Close QuizLevel2ResultActivity
            startActivity(Intent(this, QuizLevel2Activity::class.java)) // Restart QuizLevel2Activity
        }

        homeButton.setOnClickListener {
            finish() // Close QuizLevel2ResultActivity
            startActivity(Intent(this, QuizPageActivity::class.java)) // Go back to Quiz Page
        }
    }
}
