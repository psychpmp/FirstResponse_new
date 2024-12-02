package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizLevel2Activity : BaseActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answersGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var firstAidImageView: ImageView
    private lateinit var quizData: List<Triple<Int, Array<String>, Int>>
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_level2)

        supportActionBar?.hide()

        questionTextView = findViewById(R.id.questionTextView)
        answersGroup = findViewById(R.id.answersGroup)
        submitButton = findViewById(R.id.submitButton)
        firstAidImageView = findViewById(R.id.firstAidImageView)

        try {
            quizData = listOf(
                Triple(R.drawable.bandage, arrayOf("Gauze", "Scissors", "Bandage", "Adhesive Tape"), 2),
                Triple(R.drawable.antiseptic, arrayOf("Antiseptic", "Cold Compress", "Alcohol Wipes", "Safety Pins"), 0),
                Triple(R.drawable.gauze, arrayOf("Thermometer", "Pain Reliever", "Gauze", "Gloves"), 2),
                Triple(R.drawable.adhesive_tape, arrayOf("Adhesive Tape", "Emergency Blanket", "First Aid Manual", "Tweezers"), 0),
                Triple(R.drawable.scissors, arrayOf("Bandage", "Scissors", "Mask", "Alcohol Wipes"), 1),
                Triple(R.drawable.pain_reliever, arrayOf("Thermometer", "Pain Reliever", "Gauze", "Gloves"), 1),
                Triple(R.drawable.thermometer, arrayOf("Tweezers", "Cold Compress", "Thermometer", "Antiseptic"), 2),
                Triple(R.drawable.alcohol_wipes, arrayOf("Alcohol Wipes", "Emergency Blanket", "First Aid Manual", "Gloves"), 0),
                Triple(R.drawable.gloves, arrayOf("Gloves", "Mask", "Bandage", "Safety Pins"), 0),
                Triple(R.drawable.first_aid_manual, arrayOf("Pain Reliever", "Antiseptic", "First Aid Manual", "Cold Compress"), 2),
                Triple(R.drawable.cold_compress, arrayOf("Cold Compress", "Thermometer", "Tweezers", "Mask"), 0),
                Triple(R.drawable.tweezers, arrayOf("Bandage", "Tweezers", "Alcohol Wipes", "Adhesive Tape"), 1),
                Triple(R.drawable.safety_pins, arrayOf("Emergency Blanket", "Safety Pins", "Gloves", "Pain Reliever"), 1),
                Triple(R.drawable.emergency_blanket, arrayOf("Mask", "Emergency Blanket", "Gauze", "First Aid Manual"), 1),
                Triple(R.drawable.mask, arrayOf("Mask", "Adhesive Tape", "Alcohol Wipes", "Thermometer"), 0)
            ).shuffled()

            loadQuestion()

            submitButton.setOnClickListener {
                if (checkAnswer()) {
                    score++
                    Log.d("QuizLevel2Activity", "Correct Answer - Score: $score")
                } else {
                    Log.d("QuizLevel2Activity", "Incorrect Answer - Score: $score")
                }
                currentQuestionIndex++
                if (currentQuestionIndex < quizData.size) {
                    loadQuestion()
                } else {
                    showResult()
                }
            }
        } catch (e: Exception) {
            Log.e("QuizLevel2Activity", "Error initializing quiz data", e)
        }
    }

    private fun loadQuestion() {
        try {
            if (currentQuestionIndex >= quizData.size) {
                throw IndexOutOfBoundsException("Current question index out of bounds")
            }

            val (imageResId, answers, _) = quizData[currentQuestionIndex]
            firstAidImageView.setImageResource(imageResId)
            questionTextView.text = "Name the Kit!"

            answersGroup.removeAllViews()
            answers.forEachIndexed { index, answer ->
                val radioButton = RadioButton(this)
                radioButton.text = answer
                radioButton.id = View.generateViewId() // Ensure unique ID for each RadioButton
                answersGroup.addView(radioButton)
            }
        } catch (e: Exception) {
            Log.e("QuizLevel2Activity", "Error loading question", e)
        }
    }

    private fun checkAnswer(): Boolean {
        val selectedId = answersGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedId)
        val (_, answers, correctAnswerIndex) = quizData[currentQuestionIndex]

        val selectedAnswerIndex = answers.indexOf(selectedRadioButton?.text)
        Log.d("QuizLevel2Activity", "Selected Answer Index: $selectedAnswerIndex, Correct Answer Index: $correctAnswerIndex")

        return selectedAnswerIndex == correctAnswerIndex
    }

    private fun showResult() {
        val intent = Intent(this, QuizLevel2ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("isPerfectScore", score == 15) // Pass perfect score information
        startActivity(intent)
        finish() // Close QuizLevel2Activity
    }
}
