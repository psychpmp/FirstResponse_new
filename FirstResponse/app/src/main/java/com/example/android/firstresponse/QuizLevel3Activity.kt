package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizLevel3Activity : BaseActivity() {

    private lateinit var clueTextView: TextView
    private lateinit var injuryClueTextView: TextView
    private lateinit var choicesGroup: RadioGroup
    private lateinit var submitButton: Button

    private var currentQuestionIndex = 0
    private var score = 0

    // List of clues and answers (Triple of clue description, possible answers, and correct answer index)
    private lateinit var quizData: List<Triple<String, Array<String>, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_level3)

        supportActionBar?.hide()

        clueTextView = findViewById(R.id.clueTextView)
        clueTextView = findViewById(R.id.clueTextView)
        choicesGroup = findViewById(R.id.answersGroup)
        submitButton = findViewById(R.id.submitButton)

        // Initialize quiz data
        quizData = listOf(
            Triple("A deep cut is bleeding profusely. What should you do?", arrayOf("Apply a bandage", "Apply direct pressure", "Apply antiseptic", "Wait for help"), 1),
            Triple("A person has a severe burn on their arm. What is the first step?", arrayOf("Apply ice directly", "Cover with a dry cloth", "Apply an ointment", "Soak in cold water"), 3),
            Triple("Someone is choking and unable to speak. What is your immediate action?", arrayOf("Give them water", "Perform the Heimlich maneuver", "Slap their back", "Call for help"), 1),
            Triple("A person is unconscious but breathing. What position should you put them in?", arrayOf("Recovery position", "Sitting position", "Prone position", "Flat on their back"), 0),
            Triple("An individual has a sprained ankle. What should you do first?", arrayOf("Apply heat", "Immobilize the ankle", "Keep the ankle moving", "Apply pressure"), 1),
            Triple("There is a foreign object in a wound. What should you do?", arrayOf("Remove it", "Leave it in place", "Push it further in", "Ignore it"), 1),
            Triple("A person is having an asthma attack but has no inhaler. What should you do?", arrayOf("Give them water", "Have them sit down and breathe slowly", "Lie them flat", "Give them candy"), 1),
            Triple("You find someone with a suspected broken bone. What is your first action?", arrayOf("Move them", "Immobilize the limb", "Apply a hot compress", "Check for a pulse"), 1),
            Triple("A person has hypothermia. What should you do?", arrayOf("Rub their hands", "Give them a hot drink", "Move them to a warm place", "Submerge them in warm water"), 2),
            Triple("Someone is experiencing a seizure. What is your first step?", arrayOf("Hold them down", "Put something in their mouth", "Clear the area around them", "Give them water"), 2),
            Triple("A person has ingested a poison. What is the first step?", arrayOf("Give them water", "Induce vomiting", "Call poison control", "Give them food"), 2),
            Triple("Someone has a chemical burn. What is your immediate action?", arrayOf("Apply ice", "Rinse with water", "Cover with a cloth", "Apply ointment"), 1),
            Triple("A bee sting victim shows signs of an allergic reaction. What should you do?", arrayOf("Apply a cold compress", "Use an EpiPen", "Squeeze out the sting", "Give them juice"), 1),
            Triple("A person faints. What is your first action?", arrayOf("Call for medical assistance", "Splash water on them", "Elevate their legs", "Make them sit up"), 2),
            Triple("You suspect heatstroke in a person. What is the first step?", arrayOf("Give them a hot drink", "Move them to a cooler place", "Cover them with a blanket", "Give them salt tablets"), 1)
        ).shuffled() // Shuffle for randomness

        loadQuestion()

        submitButton.setOnClickListener {
            if (checkAnswer()) {
                score++
            }
            currentQuestionIndex++
            if (currentQuestionIndex < quizData.size) {
                loadQuestion()
            } else {
                showResult()
            }
        }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex >= quizData.size) return

        val (clue, answers, _) = quizData[currentQuestionIndex]
        clueTextView.text = clue // Display the clue
        choicesGroup.removeAllViews() // Clear previous answers

        answers.forEachIndexed { index, answer ->
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioButton.id = View.generateViewId() // Generate a unique ID for each RadioButton
            choicesGroup.addView(radioButton)
        }
    }

    private fun checkAnswer(): Boolean {
        val selectedId = choicesGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedId)
        val (_, _, correctAnswerIndex) = quizData[currentQuestionIndex]

        // Check if the selected answer is correct
        return selectedRadioButton != null && choicesGroup.indexOfChild(selectedRadioButton) == correctAnswerIndex
    }

    private fun showResult() {
        val intent = Intent(this, QuizLevel3ResultActivity::class.java)
        intent.putExtra("score", score)
        finish() // Close QuizActivity
        startActivity(intent) // Open QuizResultActivity
    }
}
