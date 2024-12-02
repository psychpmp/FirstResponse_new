package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class QuizLevel1Activity : BaseActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answersGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var quizData: List<Triple<String, Array<String>, Int>>
    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_level1)

        supportActionBar?.hide()

        // Initialize LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView.apply {
            setAnimation(R.raw.think) // Ensure this matches your animation file in res/raw/
            playAnimation()
            repeatCount = LottieDrawable.INFINITE // Loop animation
        }

        questionTextView = findViewById(R.id.questionTextView)
        answersGroup = findViewById(R.id.answersGroup)
        submitButton = findViewById(R.id.submitButton)

        try {
            // Initialize your quiz data
            val questionArray = resources.getStringArray(R.array.quiz_questions).toList()
            val answerArrays = listOf(
                resources.getStringArray(R.array.quiz_answers_1),
                resources.getStringArray(R.array.quiz_answers_2),
                resources.getStringArray(R.array.quiz_answers_3),
                resources.getStringArray(R.array.quiz_answers_4),
                resources.getStringArray(R.array.quiz_answers_5),
                resources.getStringArray(R.array.quiz_answers_6),
                resources.getStringArray(R.array.quiz_answers_7),
                resources.getStringArray(R.array.quiz_answers_8),
                resources.getStringArray(R.array.quiz_answers_9),
                resources.getStringArray(R.array.quiz_answers_10)
            )
            val correctAnswerArray = resources.getIntArray(R.array.correct_answers)

            if (questionArray.size != answerArrays.size || questionArray.size != correctAnswerArray.size) {
                throw IllegalStateException("Quiz data not properly initialized")
            }

            // Combine the question, answers, and correct answer into a single list of triples
            quizData = questionArray.indices.map { index ->
                Triple(questionArray[index], answerArrays[index], correctAnswerArray[index])
            }.shuffled() // Shuffle them together

            Log.d("QuizLevel1Activity", "Quiz Data: $quizData")

            loadQuestion()

            submitButton.setOnClickListener {
                if (checkAnswer()) {
                    score++
                    Log.d("QuizLevel1Activity", "Correct Answer - Score: $score")
                } else {
                    Log.d("QuizLevel1Activity", "Incorrect Answer - Score: $score")
                }
                currentQuestionIndex++
                if (currentQuestionIndex < quizData.size) {
                    loadQuestion()
                } else {
                    // Directly start the result activity instead of using setResult
                    val intent = Intent(this, QuizLevel1ResultActivity::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("isPerfectScore", score == 10) // Pass perfect score information
                    startActivity(intent)
                    finish() // Close QuizLevel1Activity
                }
            }
        } catch (e: Exception) {
            Log.e("QuizLevel1Activity", "Error initializing quiz data", e)
        }
    }

    private fun loadQuestion() {
        try {
            if (currentQuestionIndex >= quizData.size) {
                throw IndexOutOfBoundsException("Current question index out of bounds")
            }

            val (question, answers, _) = quizData[currentQuestionIndex]

            questionTextView.text = question
            answersGroup.removeAllViews()
            answers.forEachIndexed { index, answer ->
                val radioButton = RadioButton(this)
                radioButton.text = answer
                radioButton.id = View.generateViewId() // Generate a unique ID for each RadioButton
                answersGroup.addView(radioButton)
            }
        } catch (e: Exception) {
            Log.e("QuizLevel1Activity", "Error loading question", e)
        }
    }

    private fun checkAnswer(): Boolean {
        val selectedId = answersGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedId)
        val (_, answers, correctAnswerIndex) = quizData[currentQuestionIndex]

        val selectedAnswerIndex = answers.indexOf(selectedRadioButton?.text)
        Log.d("QuizLevel1Activity", "Selected Answer Index: $selectedAnswerIndex, Correct Answer Index: $correctAnswerIndex")

        return selectedAnswerIndex == correctAnswerIndex
    }
}
