package com.example.android.firstresponse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils

class WhatsWrongActivity : BaseActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionsSpinner: Spinner
    private lateinit var nextButton: Button
    private lateinit var resultTextView: TextView

    private var currentStage = 0
    private var painLocation = ""
    private var currentQuestions: List<Pair<String, List<String>>> = emptyList()
    private val answersSelected = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_wrong)

        // Initialize toolbar
        setupToolbar()

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView)
        optionsSpinner = findViewById(R.id.optionsSpinner)
        nextButton = findViewById(R.id.nextButton)
        resultTextView = findViewById(R.id.resultTextView)

        // Set up button click listener
        nextButton.setOnClickListener {
            handleNextButtonClick()
        }

        // Start by asking where the pain is located
        updateStage()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.whats_wrong)
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.red))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)

        toolbar.setNavigationOnClickListener {
            it.startAnimation(pressAnim)
            it.postDelayed({
                it.startAnimation(releaseAnim)
                finish()
            }, pressAnim.duration)
        }
    }

    private fun handleNextButtonClick() {
        val selectedOption = optionsSpinner.selectedItem?.toString() ?: return

        when (currentStage) {
            0 -> {
                // First stage: Select pain location
                painLocation = selectedOption
                if (painLocation != getString(R.string.select_an_option)) {
                    currentStage++
                    updateStage()
                } else {
                    Toast.makeText(this, getString(R.string.select_option_message), Toast.LENGTH_SHORT).show()
                }
            }
            1 -> {
                // Second stage: When the symptom happened
                val symptomTiming = selectedOption
                if (symptomTiming != getString(R.string.select_an_option)) {
                    answersSelected[getString(R.string.when_symptoms_happen)] = symptomTiming
                    currentStage++
                    updateStage()
                } else {
                    Toast.makeText(this, getString(R.string.select_option_message), Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Handle subsequent questions
                val currentQuestion = currentQuestions.getOrNull(currentStage - 2) // Adjust index for the additional question
                if (currentQuestion != null && selectedOption != getString(R.string.select_an_option)) {
                    answersSelected[currentQuestion.first] = selectedOption
                    if (currentStage < currentQuestions.size + 2) { // Adjust condition for extra stage
                        currentStage++
                        updateStage()
                    } else {
                        handleResults()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.select_option_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun updateStage() {
        when (currentStage) {
            0 -> {
                // First stage: Ask where the pain originates
                questionTextView.text = getString(R.string.where_pain_originate)
                setupSpinner(
                    listOf(
                        getString(R.string.select_an_option),
                        getString(R.string.head),
                        getString(R.string.chest),
                        getString(R.string.abdomen),
                        getString(R.string.back),
                        getString(R.string.legs),
                        getString(R.string.arms),
                        getString(R.string.joints),
                        getString(R.string.feet),
                        getString(R.string.neck),
                        getString(R.string.hands),
                        getString(R.string.stomach)
                    )
                )
            }
            1 -> {
                // Second stage: Ask when the symptom happened
                questionTextView.text = getString(R.string.when_symptoms_happen)
                setupSpinner(
                    listOf(
                        getString(R.string.select_an_option),
                        getString(R.string.less_than_24_hours),
                        getString(R.string.one_to_three_days),
                        getString(R.string.more_than_three_days),
                        getString(R.string.unknown)
                    )
                )
            }
            else -> {
                // After asking for pain location and symptom timing, proceed with specific questions based on location
                currentQuestions = getQuestionsForLocation(painLocation)
                val questionPair = currentQuestions.getOrNull(currentStage - 2) // Adjust index for the additional question
                if (questionPair != null) {
                    questionTextView.text = questionPair.first
                    setupSpinner(questionPair.second)
                } else {
                    handleResults() // Proceed to results if no more questions
                }
            }
        }
    }


    private fun getQuestionsForLocation(location: String): List<Pair<String, List<String>>> {
        return when (location) {
            getString(R.string.head) -> listOf(
                getString(R.string.head_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_2) to listOf(getString(R.string.localized), getString(R.string.diffuse)),
                getString(R.string.head_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.head_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.chest) -> listOf(
                getString(R.string.chest_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_2) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.chest_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_5) to listOf(getString(R.string.constant), getString(R.string.intermittent)),
                getString(R.string.chest_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.chest_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.abdomen) -> listOf(
                getString(R.string.abdomen_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_2) to listOf(getString(R.string.localized), getString(R.string.diffuse)),
                getString(R.string.abdomen_question_3) to listOf(getString(R.string.cramping), getString(R.string.sharp)),
                getString(R.string.abdomen_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.abdomen_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.back) -> listOf(
                getString(R.string.back_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.back_question_2) to listOf(getString(R.string.upper), getString(R.string.lower)),
                getString(R.string.back_question_3) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.back_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.back_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.back_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.back_question_7) to listOf(getString(R.string.standing), getString(R.string.sitting)),
                getString(R.string.back_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.back_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.back_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.legs) -> listOf(
                getString(R.string.legs_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_2) to listOf(getString(R.string.one), getString(R.string.both)),
                getString(R.string.legs_question_3) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.legs_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.legs_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.arms) -> listOf(
                getString(R.string.arms_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_2) to listOf(getString(R.string.one), getString(R.string.both)),
                getString(R.string.arms_question_3) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.arms_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.arms_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.joints) -> listOf(
                getString(R.string.joints_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_2) to listOf(getString(R.string.one), getString(R.string.multiple)),
                getString(R.string.joints_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_5) to listOf(getString(R.string.constant), getString(R.string.intermittent)),
                getString(R.string.joints_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.joints_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.feet) -> listOf(
                getString(R.string.feet_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_2) to listOf(getString(R.string.one), getString(R.string.both)),
                getString(R.string.feet_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_5) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.feet_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.feet_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.neck) -> listOf(
                getString(R.string.neck_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_2) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.neck_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.neck_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.hands) -> listOf(
                getString(R.string.hands_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_2) to listOf(getString(R.string.one), getString(R.string.both)),
                getString(R.string.hands_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_4) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_5) to listOf(getString(R.string.sharp), getString(R.string.dull)),
                getString(R.string.hands_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.hands_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            getString(R.string.stomach) -> listOf(
                getString(R.string.stomach_question_1) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_2) to listOf(getString(R.string.sharp), getString(R.string.cramping)),
                getString(R.string.stomach_question_3) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_4) to listOf(getString(R.string.constant), getString(R.string.intermittent)),
                getString(R.string.stomach_question_5) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_6) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_7) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_8) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_9) to listOf(getString(R.string.yes), getString(R.string.no)),
                getString(R.string.stomach_question_10) to listOf(getString(R.string.yes), getString(R.string.no))
            )
            else -> emptyList()
        }
    }

    private fun setupSpinner(options: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, options)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        optionsSpinner.adapter = adapter
    }

    private fun handleResults() {
        val possibleConditions = calculateDiagnosis()

        resultTextView.text = possibleConditions
        resultTextView.visibility = View.VISIBLE

        questionTextView.visibility = View.GONE
        optionsSpinner.visibility = View.GONE
        nextButton.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            showAlert()
        }, 1000)
    }

    private fun calculateDiagnosis(): String {
        val diagnosisOptions = mapOf(
            getString(R.string.head) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.head_question_1) to getString(R.string.yes),
                    getString(R.string.head_question_2) to getString(R.string.localized),
                    getString(R.string.head_question_3) to getString(R.string.yes),
                    getString(R.string.head_question_4) to getString(R.string.yes),
                    getString(R.string.head_question_5) to getString(R.string.yes),
                    getString(R.string.head_question_6) to getString(R.string.yes),
                    getString(R.string.head_question_7) to getString(R.string.yes),
                    getString(R.string.head_question_8) to getString(R.string.no),
                    getString(R.string.head_question_9) to getString(R.string.no),
                    getString(R.string.head_question_10) to getString(R.string.yes)
                ) to getString(R.string.migraine),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.head_question_1) to getString(R.string.yes),
                    getString(R.string.head_question_2) to getString(R.string.diffuse),
                    getString(R.string.head_question_3) to getString(R.string.no),
                    getString(R.string.head_question_4) to getString(R.string.no),
                    getString(R.string.head_question_5) to getString(R.string.no),
                    getString(R.string.head_question_6) to getString(R.string.yes),
                    getString(R.string.head_question_7) to getString(R.string.yes),
                    getString(R.string.head_question_8) to getString(R.string.no),
                    getString(R.string.head_question_9) to getString(R.string.no),
                    getString(R.string.head_question_10) to getString(R.string.no)
                ) to getString(R.string.tension_headache),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.head_question_1) to getString(R.string.yes),
                    getString(R.string.head_question_2) to getString(R.string.localized),
                    getString(R.string.head_question_3) to getString(R.string.no),
                    getString(R.string.head_question_4) to getString(R.string.no),
                    getString(R.string.head_question_5) to getString(R.string.yes),
                    getString(R.string.head_question_6) to getString(R.string.yes),
                    getString(R.string.head_question_7) to getString(R.string.no),
                    getString(R.string.head_question_8) to getString(R.string.yes),
                    getString(R.string.head_question_9) to getString(R.string.yes),
                    getString(R.string.head_question_10) to getString(R.string.yes)
                ) to getString(R.string.concussion),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.head_question_1) to getString(R.string.yes),
                    getString(R.string.head_question_2) to getString(R.string.localized),
                    getString(R.string.head_question_3) to getString(R.string.no),
                    getString(R.string.head_question_4) to getString(R.string.yes),
                    getString(R.string.head_question_5) to getString(R.string.yes),
                    getString(R.string.head_question_6) to getString(R.string.yes),
                    getString(R.string.head_question_7) to getString(R.string.no),
                    getString(R.string.head_question_8) to getString(R.string.yes),
                    getString(R.string.head_question_9) to getString(R.string.yes),
                    getString(R.string.head_question_10) to getString(R.string.yes)
                ) to getString(R.string.meningitis),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.head_question_1) to getString(R.string.yes),
                    getString(R.string.head_question_2) to getString(R.string.localized),
                    getString(R.string.head_question_3) to getString(R.string.yes),
                    getString(R.string.head_question_4) to getString(R.string.no),
                    getString(R.string.head_question_5) to getString(R.string.yes),
                    getString(R.string.head_question_6) to getString(R.string.no),
                    getString(R.string.head_question_7) to getString(R.string.no),
                    getString(R.string.head_question_8) to getString(R.string.yes),
                    getString(R.string.head_question_9) to getString(R.string.yes),
                    getString(R.string.head_question_10) to getString(R.string.yes)
                ) to getString(R.string.stroke)
            ),
            getString(R.string.chest) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.chest_question_3) to getString(R.string.yes), // "Does the pain radiate to other areas?" to "Yes"
                    getString(R.string.chest_question_4) to getString(R.string.no), // "Do you have difficulty breathing?" to "No"
                    getString(R.string.chest_question_5) to getString(R.string.constant), // "Is the pain constant or intermittent?" to "Constant"
                    getString(R.string.chest_question_6) to getString(R.string.no), // "Do you experience nausea?" to "No"
                    getString(R.string.chest_question_7) to getString(R.string.yes), // "Is the pain related to physical activity?" to "Yes"
                    getString(R.string.chest_question_8) to getString(R.string.no), // "Do you have a history of any chronic health conditions?" to "No"
                    getString(R.string.chest_question_9) to getString(R.string.no), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "No"
                    getString(R.string.chest_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.angina), // "Angina"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.chest_question_3) to getString(R.string.yes), // "Does the pain radiate to other areas?" to "Yes"
                    getString(R.string.chest_question_4) to getString(R.string.yes), // "Do you have difficulty breathing?" to "Yes"
                    getString(R.string.chest_question_5) to getString(R.string.constant), // "Is the pain constant or intermittent?" to "Constant"
                    getString(R.string.chest_question_6) to getString(R.string.yes), // "Do you experience nausea?" to "Yes"
                    getString(R.string.chest_question_7) to getString(R.string.no), // "Is the pain related to physical activity?" to "No"
                    getString(R.string.chest_question_8) to getString(R.string.yes), // "Do you have a history of any chronic health conditions?" to "Yes"
                    getString(R.string.chest_question_9) to getString(R.string.yes), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes"
                    getString(R.string.chest_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.heart_attack), // "Heart Attack"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.dull), // "Is the pain sharp or dull?" to "Dull"
                    getString(R.string.chest_question_3) to getString(R.string.no), // "Does the pain radiate to other areas?" to "No"
                    getString(R.string.chest_question_4) to getString(R.string.yes), // "Do you have difficulty breathing?" to "Yes"
                    getString(R.string.chest_question_5) to getString(R.string.intermittent), // "Is the pain constant or intermittent?" to "Intermittent"
                    getString(R.string.chest_question_6) to getString(R.string.no), // "Do you experience nausea?" to "No"
                    getString(R.string.chest_question_7) to getString(R.string.no), // "Is the pain related to physical activity?" to "No"
                    getString(R.string.chest_question_8) to getString(R.string.no), // "Do you have a history of any chronic health conditions?" to "No"
                    getString(R.string.chest_question_9) to getString(R.string.no), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "No"
                    getString(R.string.chest_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.gerd), // "Gastroesophageal Reflux Disease (GERD)"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.chest_question_3) to getString(R.string.no), // "Does the pain radiate to other areas?" to "No"
                    getString(R.string.chest_question_4) to getString(R.string.no), // "Do you have difficulty breathing?" to "No"
                    getString(R.string.chest_question_5) to getString(R.string.constant), // "Is the pain constant or intermittent?" to "Constant"
                    getString(R.string.chest_question_6) to getString(R.string.yes), // "Do you experience nausea?" to "Yes"
                    getString(R.string.chest_question_7) to getString(R.string.no), // "Is the pain related to physical activity?" to "No"
                    getString(R.string.chest_question_8) to getString(R.string.no), // "Do you have a history of any chronic health conditions?" to "No"
                    getString(R.string.chest_question_9) to getString(R.string.yes), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes"
                    getString(R.string.chest_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.panic_attack_condition), // "Panic Attack"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.dull), // "Is the pain sharp or dull?" to "Dull"
                    getString(R.string.chest_question_3) to getString(R.string.yes), // "Does the pain radiate to other areas?" to "Yes"
                    getString(R.string.chest_question_4) to getString(R.string.yes), // "Do you have difficulty breathing?" to "Yes"
                    getString(R.string.chest_question_5) to getString(R.string.intermittent), // "Is the pain constant or intermittent?" to "Intermittent"
                    getString(R.string.chest_question_6) to getString(R.string.no), // "Do you experience nausea?" to "No"
                    getString(R.string.chest_question_7) to getString(R.string.no), // "Is the pain related to physical activity?" to "No"
                    getString(R.string.chest_question_8) to getString(R.string.no), // "Do you have a history of any chronic health conditions?" to "No"
                    getString(R.string.chest_question_9) to getString(R.string.yes), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes"
                    getString(R.string.chest_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.pulmonary_embolism), // "Pulmonary Embolism"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.dull), // "Is the pain sharp or dull?" to "Dull"
                    getString(R.string.chest_question_3) to getString(R.string.no), // "Does the pain radiate to other areas?" to "No"
                    getString(R.string.chest_question_4) to getString(R.string.no), // "Do you have difficulty breathing?" to "No"
                    getString(R.string.chest_question_5) to getString(R.string.constant), // "Is the pain constant or intermittent?" to "Constant"
                    getString(R.string.chest_question_6) to getString(R.string.no), // "Do you experience nausea?" to "No"
                    getString(R.string.chest_question_7) to getString(R.string.yes), // "Is the pain related to eating?" to "Yes"
                    getString(R.string.chest_question_8) to getString(R.string.yes), // "Do you have a history of any chronic health conditions?" to "Yes"
                    getString(R.string.chest_question_9) to getString(R.string.yes), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes"
                    getString(R.string.chest_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.peptic_ulcer), // "Peptic Ulcer"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.chest_question_3) to getString(R.string.no), // "Does the pain radiate to other areas?" to "No"
                    getString(R.string.chest_question_4) to getString(R.string.yes), // "Do you have difficulty breathing?" to "Yes"
                    getString(R.string.chest_question_5) to getString(R.string.intermittent), // "Is the pain constant or intermittent?" to "Intermittent"
                    getString(R.string.chest_question_6) to getString(R.string.yes), // "Do you experience shortness of breath?" to "Yes"
                    getString(R.string.chest_question_7) to getString(R.string.yes), // "Is the pain related to cold weather?" to "Yes"
                    getString(R.string.chest_question_8) to getString(R.string.yes), // "Do you have a history of any chronic health conditions?" to "Yes"
                    getString(R.string.chest_question_9) to getString(R.string.yes), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "Yes"
                    getString(R.string.chest_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.asthma), // "Asthma"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.chest_question_1) to getString(R.string.yes), // "Do you have chest pain?" to "Yes"
                    getString(R.string.chest_question_2) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.chest_question_3) to getString(R.string.yes), // "Does the pain radiate to other areas?" to "Yes"
                    getString(R.string.chest_question_4) to getString(R.string.no), // "Do you have difficulty breathing?" to "No"
                    getString(R.string.chest_question_5) to getString(R.string.intermittent), // "Is the pain constant or intermittent?" to "Intermittent"
                    getString(R.string.chest_question_6) to getString(R.string.no), // "Do you experience nausea?" to "No"
                    getString(R.string.chest_question_7) to getString(R.string.yes), // "Is the pain related to physical activity?" to "Yes"
                    getString(R.string.chest_question_8) to getString(R.string.yes), // "Do you have a history of any chronic health conditions?" to "Yes"
                    getString(R.string.chest_question_9) to getString(R.string.no), // "Are you experiencing sweating/bloating/wheezing/tenderness?" to "No"
                    getString(R.string.chest_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.muscle_strain) // "Muscle Strain"
            ),
            getString(R.string.abdomen) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.localized), // "Is the pain localized or diffuse?" to "Localized"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.no), // "Is there any change in bowel movements?" to "No"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.gastritis), // "Gastritis"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.diffuse), // "Is the pain localized or diffuse?" to "Diffuse"
                    getString(R.string.abdomen_question_3) to getString(R.string.sharp), // "Is the pain cramping or sharp?" to "Sharp"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.yes), // "Is there blood in the stool?" to "Yes"
                    getString(R.string.abdomen_question_8) to getString(R.string.yes), // "Do you have a fever?" to "Yes"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.appendicitis), // "Appendicitis"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.localized), // "Is the pain localized or diffuse?" to "Localized"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.no), // "Do you experience bloating?" to "No"
                    getString(R.string.abdomen_question_6) to getString(R.string.no), // "Is there any change in bowel movements?" to "No"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.ibs), // "Irritable Bowel Syndrome (IBS)"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.localized), // "Is the pain localized or diffuse?" to "Localized"
                    getString(R.string.abdomen_question_3) to getString(R.string.sharp), // "Is the pain cramping or sharp?" to "Sharp"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.peptic_ulcer), // "Peptic Ulcer Disease"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.diffuse), // "Is the pain localized or diffuse?" to "Diffuse"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.yes), // "Do you have a fever?" to "Yes"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.diverticulitis), // "Diverticulitis"
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.diffuse), // "Is the pain localized or diffuse?" to "Diffuse"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.no), // "Is there any change in bowel movements?" to "No"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.yes), // "Do you feel better after eating?" to "Yes"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.constipation), // "Constipation"

                // Food Poisoning
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.localized), // "Is the pain localized or diffuse?" to "Localized"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.yes), // "Do you have a fever?" to "Yes"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.food_poisoning), // "Food Poisoning"

                // Inflammatory Bowel Disease (IBD)
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.diffuse), // "Is the pain localized or diffuse?" to "Diffuse"
                    getString(R.string.abdomen_question_3) to getString(R.string.sharp), // "Is the pain cramping or sharp?" to "Sharp"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.no), // "Do you experience bloating?" to "No"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.yes), // "Is there blood in the stool?" to "Yes"
                    getString(R.string.abdomen_question_8) to getString(R.string.yes), // "Do you have a fever?" to "Yes"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.ibd), // "Inflammatory Bowel Disease (IBD)"

                // Functional Dyspepsia
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.localized), // "Is the pain localized or diffuse?" to "Localized"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.no), // "Do you have nausea or vomiting?" to "No"
                    getString(R.string.abdomen_question_5) to getString(R.string.no), // "Do you experience bloating?" to "No"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.yes), // "Do you feel better after eating?" to "Yes"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.functional_dyspepsia), // "Functional Dyspepsia"
                // Celiac Disease
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.diffuse), // "Is the pain localized or diffuse?" to "Diffuse"
                    getString(R.string.abdomen_question_3) to getString(R.string.sharp), // "Is the pain cramping or sharp?" to "Sharp"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.yes), // "Do you experience bloating?" to "Yes"
                    getString(R.string.abdomen_question_6) to getString(R.string.no), // "Is there any change in bowel movements?" to "No"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.no), // "Do you feel better after eating?" to "No"
                    getString(R.string.abdomen_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.celiac_disease), // "Celiac Disease"

                // Lactose Intolerance
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.abdomen_question_1) to getString(R.string.yes), // "Do you have abdominal pain?" to "Yes"
                    getString(R.string.abdomen_question_2) to getString(R.string.localized), // "Is the pain localized or diffuse?" to "Localized"
                    getString(R.string.abdomen_question_3) to getString(R.string.cramping), // "Is the pain cramping or sharp?" to "Cramping"
                    getString(R.string.abdomen_question_4) to getString(R.string.yes), // "Do you have nausea or vomiting?" to "Yes"
                    getString(R.string.abdomen_question_5) to getString(R.string.no), // "Do you experience bloating?" to "No"
                    getString(R.string.abdomen_question_6) to getString(R.string.yes), // "Is there any change in bowel movements?" to "Yes"
                    getString(R.string.abdomen_question_7) to getString(R.string.no), // "Is there blood in the stool?" to "No"
                    getString(R.string.abdomen_question_8) to getString(R.string.no), // "Do you have a fever?" to "No"
                    getString(R.string.abdomen_question_9) to getString(R.string.yes), // "Do you feel better after eating?" to "Yes"
                    getString(R.string.abdomen_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.lactose_intolerance)
            ),
            getString(R.string.back) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes), // "Do you have back pain?" to "Yes"
                    getString(R.string.back_question_2) to getString(R.string.lower), // "Is the pain in the upper or lower back?" to "Lower"
                    getString(R.string.back_question_3) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.back_question_4) to getString(R.string.yes), // "Does the pain radiate to the legs?" to "Yes"
                    getString(R.string.back_question_5) to getString(R.string.no), // "Do you have any weakness in the legs?" to "No"
                    getString(R.string.back_question_6) to getString(R.string.yes), // "Do you have difficulty walking?" to "Yes"
                    getString(R.string.back_question_7) to getString(R.string.standing), // "Is the pain worse when standing or sitting?" to "Standing"
                    getString(R.string.back_question_8) to getString(R.string.yes), // "Do you have a history of back problems?" to "Yes"
                    getString(R.string.back_question_9) to getString(R.string.yes), // "Do you experience tingling or numbness?" to "Yes"
                    getString(R.string.back_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.herniated_disc), // "Herniated Disc"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.upper), // "Is the pain in the upper or lower back?" to "Upper"
                    getString(R.string.back_question_3) to getString(R.string.dull), // "Is the pain sharp or dull?" to "Dull"
                    getString(R.string.back_question_4) to getString(R.string.no), // "Does the pain radiate to the legs?" to "No"
                    getString(R.string.back_question_5) to getString(R.string.no), // "Do you have any weakness in the legs?" to "No"
                    getString(R.string.back_question_6) to getString(R.string.no), // "Do you have difficulty walking?" to "No"
                    getString(R.string.back_question_7) to getString(R.string.sitting), // "Is the pain worse when standing or sitting?" to "Sitting"
                    getString(R.string.back_question_8) to getString(R.string.yes), // "Do you have a history of back problems?" to "Yes"
                    getString(R.string.back_question_9) to getString(R.string.no), // "Do you experience tingling or numbness?" to "No"
                    getString(R.string.back_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.muscle_strain), // "Muscle Strain"
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.lower),
                    getString(R.string.back_question_3) to getString(R.string.sharp),
                    getString(R.string.back_question_4) to getString(R.string.yes),
                    getString(R.string.back_question_5) to getString(R.string.yes),
                    getString(R.string.back_question_6) to getString(R.string.yes),
                    getString(R.string.back_question_7) to getString(R.string.standing),
                    getString(R.string.back_question_8) to getString(R.string.no),
                    getString(R.string.back_question_9) to getString(R.string.yes),
                    getString(R.string.back_question_10) to getString(R.string.yes)
                ) to getString(R.string.spinal_stenosis), // "Spinal Stenosis"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.upper),
                    getString(R.string.back_question_3) to getString(R.string.sharp),
                    getString(R.string.back_question_4) to getString(R.string.no),
                    getString(R.string.back_question_5) to getString(R.string.no),
                    getString(R.string.back_question_6) to getString(R.string.no),
                    getString(R.string.back_question_7) to getString(R.string.sitting),
                    getString(R.string.back_question_8) to getString(R.string.no),
                    getString(R.string.back_question_9) to getString(R.string.no),
                    getString(R.string.back_question_10) to getString(R.string.yes)
                ) to getString(R.string.postural_issues), // "Postural Issues"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.lower),
                    getString(R.string.back_question_3) to getString(R.string.sharp),
                    getString(R.string.back_question_4) to getString(R.string.yes),
                    getString(R.string.back_question_5) to getString(R.string.yes),
                    getString(R.string.back_question_6) to getString(R.string.no),
                    getString(R.string.back_question_7) to getString(R.string.standing),
                    getString(R.string.back_question_8) to getString(R.string.yes),
                    getString(R.string.back_question_9) to getString(R.string.no),
                    getString(R.string.back_question_10) to getString(R.string.no)
                ) to getString(R.string.sciatica), // "Sciatica"
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.lower),
                    getString(R.string.back_question_3) to getString(R.string.dull),
                    getString(R.string.back_question_4) to getString(R.string.no),
                    getString(R.string.back_question_5) to getString(R.string.no),
                    getString(R.string.back_question_6) to getString(R.string.no),
                    getString(R.string.back_question_7) to getString(R.string.standing),
                    getString(R.string.back_question_8) to getString(R.string.no),
                    getString(R.string.back_question_9) to getString(R.string.yes),
                    getString(R.string.back_question_10) to getString(R.string.yes)
                ) to getString(R.string.kidney_stones), // "Kidney Stones"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.upper),
                    getString(R.string.back_question_3) to getString(R.string.sharp),
                    getString(R.string.back_question_4) to getString(R.string.no),
                    getString(R.string.back_question_5) to getString(R.string.no),
                    getString(R.string.back_question_6) to getString(R.string.no),
                    getString(R.string.back_question_7) to getString(R.string.sitting),
                    getString(R.string.back_question_8) to getString(R.string.yes),
                    getString(R.string.back_question_9) to getString(R.string.no),
                    getString(R.string.back_question_10) to getString(R.string.no)
                ) to getString(R.string.thoracic_outlet_syndrome), // "Thoracic Outlet Syndrome"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.lower),
                    getString(R.string.back_question_3) to getString(R.string.dull),
                    getString(R.string.back_question_4) to getString(R.string.yes),
                    getString(R.string.back_question_5) to getString(R.string.yes),
                    getString(R.string.back_question_6) to getString(R.string.yes),
                    getString(R.string.back_question_7) to getString(R.string.standing),
                    getString(R.string.back_question_8) to getString(R.string.yes),
                    getString(R.string.back_question_9) to getString(R.string.yes),
                    getString(R.string.back_question_10) to getString(R.string.yes)
                ) to getString(R.string.degenerative_disc_disease), // "Degenerative Disc Disease"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.upper),
                    getString(R.string.back_question_3) to getString(R.string.sharp),
                    getString(R.string.back_question_4) to getString(R.string.no),
                    getString(R.string.back_question_5) to getString(R.string.no),
                    getString(R.string.back_question_6) to getString(R.string.no),
                    getString(R.string.back_question_7) to getString(R.string.sitting),
                    getString(R.string.back_question_8) to getString(R.string.no),
                    getString(R.string.back_question_9) to getString(R.string.no),
                    getString(R.string.back_question_10) to getString(R.string.no)
                ) to getString(R.string.upper_back_pain), // "Upper Back Pain"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.back_question_1) to getString(R.string.yes),
                    getString(R.string.back_question_2) to getString(R.string.lower),
                    getString(R.string.back_question_3) to getString(R.string.dull),
                    getString(R.string.back_question_4) to getString(R.string.no),
                    getString(R.string.back_question_5) to getString(R.string.no),
                    getString(R.string.back_question_6) to getString(R.string.no),
                    getString(R.string.back_question_7) to getString(R.string.standing),
                    getString(R.string.back_question_8) to getString(R.string.no),
                    getString(R.string.back_question_9) to getString(R.string.yes),
                    getString(R.string.back_question_10) to getString(R.string.no)
                ) to getString(R.string.muscle_spasm) // "Muscle Spasm"
            ),
            getString(R.string.legs) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes), // "Do you have leg pain?" to "Yes"
                    getString(R.string.legs_question_2) to getString(R.string.one), // "Is the pain in one or both legs?" to "One"
                    getString(R.string.legs_question_3) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.legs_question_4) to getString(R.string.yes), // "Do you have swelling in the legs?" to "Yes"
                    getString(R.string.legs_question_5) to getString(R.string.no), // "Do you experience cramping?" to "No"
                    getString(R.string.legs_question_6) to getString(R.string.yes), // "Is the pain worse when walking?" to "Yes"
                    getString(R.string.legs_question_7) to getString(R.string.no), // "Do you have a history of leg injuries?" to "No"
                    getString(R.string.legs_question_8) to getString(R.string.yes), // "Do you experience tingling or numbness?" to "Yes"
                    getString(R.string.legs_question_9) to getString(R.string.no), // "Is there any change in skin color?" to "No"
                    getString(R.string.legs_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.deep_vein_thrombosis), // "Deep Vein Thrombosis (DVT)"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.both), // "Is the pain in one or both legs?" to "Both"
                    getString(R.string.legs_question_3) to getString(R.string.dull), // "Is the pain sharp or dull?" to "Dull"
                    getString(R.string.legs_question_4) to getString(R.string.yes),
                    getString(R.string.legs_question_5) to getString(R.string.yes),
                    getString(R.string.legs_question_6) to getString(R.string.no), // "Is the pain worse when walking?" to "No"
                    getString(R.string.legs_question_7) to getString(R.string.no),
                    getString(R.string.legs_question_8) to getString(R.string.no),
                    getString(R.string.legs_question_9) to getString(R.string.yes), // "Is there any change in skin color?" to "Yes"
                    getString(R.string.legs_question_10) to getString(R.string.yes) // "Do you have any other symptoms?" to "Yes"
                ) to getString(R.string.peripheral_artery_disease), // "Peripheral Artery Disease (PAD)"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.one),
                    getString(R.string.legs_question_3) to getString(R.string.sharp),
                    getString(R.string.legs_question_4) to getString(R.string.no),
                    getString(R.string.legs_question_5) to getString(R.string.no),
                    getString(R.string.legs_question_6) to getString(R.string.yes),
                    getString(R.string.legs_question_7) to getString(R.string.yes),
                    getString(R.string.legs_question_8) to getString(R.string.yes),
                    getString(R.string.legs_question_9) to getString(R.string.no),
                    getString(R.string.legs_question_10) to getString(R.string.no)
                ) to getString(R.string.muscle_strain), // "Muscle Strain"
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.both),
                    getString(R.string.legs_question_3) to getString(R.string.dull),
                    getString(R.string.legs_question_4) to getString(R.string.yes),
                    getString(R.string.legs_question_5) to getString(R.string.yes),
                    getString(R.string.legs_question_6) to getString(R.string.yes),
                    getString(R.string.legs_question_7) to getString(R.string.no),
                    getString(R.string.legs_question_8) to getString(R.string.no),
                    getString(R.string.legs_question_9) to getString(R.string.no),
                    getString(R.string.legs_question_10) to getString(R.string.no)
                ) to getString(R.string.varicose_veins),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.one),
                    getString(R.string.legs_question_3) to getString(R.string.dull),
                    getString(R.string.legs_question_4) to getString(R.string.no),
                    getString(R.string.legs_question_5) to getString(R.string.yes),
                    getString(R.string.legs_question_6) to getString(R.string.no),
                    getString(R.string.legs_question_7) to getString(R.string.yes),
                    getString(R.string.legs_question_8) to getString(R.string.yes),
                    getString(R.string.legs_question_9) to getString(R.string.no),
                    getString(R.string.legs_question_10) to getString(R.string.yes)
                ) to getString(R.string.sciatica),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.both),
                    getString(R.string.legs_question_3) to getString(R.string.sharp),
                    getString(R.string.legs_question_4) to getString(R.string.no),
                    getString(R.string.legs_question_5) to getString(R.string.no),
                    getString(R.string.legs_question_6) to getString(R.string.yes),
                    getString(R.string.legs_question_7) to getString(R.string.no),
                    getString(R.string.legs_question_8) to getString(R.string.no),
                    getString(R.string.legs_question_9) to getString(R.string.yes),
                    getString(R.string.legs_question_10) to getString(R.string.no)
                ) to getString(R.string.cellulitis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.one),
                    getString(R.string.legs_question_3) to getString(R.string.dull),
                    getString(R.string.legs_question_4) to getString(R.string.yes),
                    getString(R.string.legs_question_5) to getString(R.string.no),
                    getString(R.string.legs_question_6) to getString(R.string.no),
                    getString(R.string.legs_question_7) to getString(R.string.yes),
                    getString(R.string.legs_question_8) to getString(R.string.no),
                    getString(R.string.legs_question_9) to getString(R.string.yes),
                    getString(R.string.legs_question_10) to getString(R.string.yes)
                ) to getString(R.string.chronic_venous_insufficiency),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.legs_question_1) to getString(R.string.yes),
                    getString(R.string.legs_question_2) to getString(R.string.both),
                    getString(R.string.legs_question_3) to getString(R.string.dull),
                    getString(R.string.legs_question_4) to getString(R.string.no),
                    getString(R.string.legs_question_5) to getString(R.string.yes),
                    getString(R.string.legs_question_6) to getString(R.string.no),
                    getString(R.string.legs_question_7) to getString(R.string.no),
                    getString(R.string.legs_question_8) to getString(R.string.no),
                    getString(R.string.legs_question_9) to getString(R.string.yes),
                    getString(R.string.legs_question_10) to getString(R.string.no)
                ) to getString(R.string.varicose_veins)
            ),
            getString(R.string.arms) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.arms_question_1) to getString(R.string.yes), // "Do you have arm pain?" to "Yes"
                    getString(R.string.arms_question_2) to getString(R.string.one), // "Is the pain in one or both arms?" to "One"
                    getString(R.string.arms_question_3) to getString(R.string.sharp), // "Is the pain sharp or dull?" to "Sharp"
                    getString(R.string.arms_question_4) to getString(R.string.yes), // "Do you have weakness in the arms?" to "Yes"
                    getString(R.string.arms_question_5) to getString(R.string.yes), // "Do you experience tingling or numbness?" to "Yes"
                    getString(R.string.arms_question_6) to getString(R.string.no), // "Is the pain related to physical activity?" to "No"
                    getString(R.string.arms_question_7) to getString(R.string.no), // "Do you have swelling in the arms?" to "No"
                    getString(R.string.arms_question_8) to getString(R.string.no), // "Do you experience muscle cramps?" to "No"
                    getString(R.string.arms_question_9) to getString(R.string.no), // "Do you have difficulty moving the arms?" to "No"
                    getString(R.string.arms_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.muscle_strain), // "Muscle Strain"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.both), // "Is the pain in one or both arms?" to "Both"
                    getString(R.string.arms_question_3) to getString(R.string.dull), // "Is the pain sharp or dull?" to "Dull"
                    getString(R.string.arms_question_4) to getString(R.string.yes),
                    getString(R.string.arms_question_5) to getString(R.string.yes),
                    getString(R.string.arms_question_6) to getString(R.string.yes), // "Is the pain related to physical activity?" to "Yes"
                    getString(R.string.arms_question_7) to getString(R.string.yes), // "Do you have swelling in the arms?" to "Yes"
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.yes), // "Do you have difficulty moving the arms?" to "Yes"
                    getString(R.string.arms_question_10) to getString(R.string.no) // "Do you have any other symptoms?" to "No"
                ) to getString(R.string.tendinitis), // "Tendinitis"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.one),
                    getString(R.string.arms_question_3) to getString(R.string.sharp),
                    getString(R.string.arms_question_4) to getString(R.string.no),
                    getString(R.string.arms_question_5) to getString(R.string.no),
                    getString(R.string.arms_question_6) to getString(R.string.no),
                    getString(R.string.arms_question_7) to getString(R.string.yes), // "Do you have swelling in the arms?" to "Yes"
                    getString(R.string.arms_question_8) to getString(R.string.yes), // "Do you experience muscle cramps?" to "Yes"
                    getString(R.string.arms_question_9) to getString(R.string.no),
                    getString(R.string.arms_question_10) to getString(R.string.no)
                ) to getString(R.string.overuse_syndrome), // "Overuse Syndrome"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.both),
                    getString(R.string.arms_question_3) to getString(R.string.dull),
                    getString(R.string.arms_question_4) to getString(R.string.yes),
                    getString(R.string.arms_question_5) to getString(R.string.yes),
                    getString(R.string.arms_question_6) to getString(R.string.yes),
                    getString(R.string.arms_question_7) to getString(R.string.no),
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.yes),
                    getString(R.string.arms_question_10) to getString(R.string.no)
                ) to getString(R.string.carpal_tunnel_syndrome), // "Carpal Tunnel Syndrome"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.one),
                    getString(R.string.arms_question_3) to getString(R.string.sharp),
                    getString(R.string.arms_question_4) to getString(R.string.yes),
                    getString(R.string.arms_question_5) to getString(R.string.yes),
                    getString(R.string.arms_question_6) to getString(R.string.no),
                    getString(R.string.arms_question_7) to getString(R.string.no),
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.no),
                    getString(R.string.arms_question_10) to getString(R.string.no)
                ) to getString(R.string.bursitis), // "Bursitis"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.both),
                    getString(R.string.arms_question_3) to getString(R.string.dull),
                    getString(R.string.arms_question_4) to getString(R.string.no),
                    getString(R.string.arms_question_5) to getString(R.string.yes),
                    getString(R.string.arms_question_6) to getString(R.string.no),
                    getString(R.string.arms_question_7) to getString(R.string.no),
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.yes),
                    getString(R.string.arms_question_10) to getString(R.string.no)
                ) to getString(R.string.swelling),// "Swelling"
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.one),
                    getString(R.string.arms_question_3) to getString(R.string.dull),
                    getString(R.string.arms_question_4) to getString(R.string.yes),
                    getString(R.string.arms_question_5) to getString(R.string.no),
                    getString(R.string.arms_question_6) to getString(R.string.yes),
                    getString(R.string.arms_question_7) to getString(R.string.no),
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.yes),
                    getString(R.string.arms_question_10) to getString(R.string.yes)
                ) to getString(R.string.nerve_compression), // "Nerve Compression"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.both),
                    getString(R.string.arms_question_3) to getString(R.string.sharp),
                    getString(R.string.arms_question_4) to getString(R.string.yes),
                    getString(R.string.arms_question_5) to getString(R.string.yes),
                    getString(R.string.arms_question_6) to getString(R.string.no),
                    getString(R.string.arms_question_7) to getString(R.string.yes),
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.yes),
                    getString(R.string.arms_question_10) to getString(R.string.yes)
                ) to getString(R.string.frozen_shoulder), // "Frozen Shoulder"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.one),
                    getString(R.string.arms_question_3) to getString(R.string.dull),
                    getString(R.string.arms_question_4) to getString(R.string.yes),
                    getString(R.string.arms_question_5) to getString(R.string.yes),
                    getString(R.string.arms_question_6) to getString(R.string.yes),
                    getString(R.string.arms_question_7) to getString(R.string.no),
                    getString(R.string.arms_question_8) to getString(R.string.no),
                    getString(R.string.arms_question_9) to getString(R.string.yes),
                    getString(R.string.arms_question_10) to getString(R.string.no)
                ) to getString(R.string.tendinitis), // "Tendinitis"

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.arms_question_1) to getString(R.string.yes),
                    getString(R.string.arms_question_2) to getString(R.string.both),
                    getString(R.string.arms_question_3) to getString(R.string.sharp),
                    getString(R.string.arms_question_4) to getString(R.string.no),
                    getString(R.string.arms_question_5) to getString(R.string.no),
                    getString(R.string.arms_question_6) to getString(R.string.no),
                    getString(R.string.arms_question_7) to getString(R.string.yes),
                    getString(R.string.arms_question_8) to getString(R.string.yes),
                    getString(R.string.arms_question_9) to getString(R.string.no),
                    getString(R.string.arms_question_10) to getString(R.string.no)
                ) to getString(R.string.myopathy) // "Myopathy"
            ),
            getString(R.string.joints) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.one),
                    getString(R.string.joints_question_3) to getString(R.string.yes),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.constant),
                    getString(R.string.joints_question_6) to getString(R.string.no),
                    getString(R.string.joints_question_7) to getString(R.string.yes),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.no)
                ) to getString(R.string.osteoarthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.multiple),
                    getString(R.string.joints_question_3) to getString(R.string.yes),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.intermittent),
                    getString(R.string.joints_question_6) to getString(R.string.yes),
                    getString(R.string.joints_question_7) to getString(R.string.yes),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.no)
                ) to getString(R.string.rheumatoid_arthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.one),
                    getString(R.string.joints_question_3) to getString(R.string.no),
                    getString(R.string.joints_question_4) to getString(R.string.no),
                    getString(R.string.joints_question_5) to getString(R.string.intermittent),
                    getString(R.string.joints_question_6) to getString(R.string.no),
                    getString(R.string.joints_question_7) to getString(R.string.no),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.no),
                    getString(R.string.joints_question_10) to getString(R.string.no)
                ) to getString(R.string.tendinitis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.multiple),
                    getString(R.string.joints_question_3) to getString(R.string.yes),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.constant),
                    getString(R.string.joints_question_6) to getString(R.string.yes),
                    getString(R.string.joints_question_7) to getString(R.string.no),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.yes)
                ) to getString(R.string.gout),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.one),
                    getString(R.string.joints_question_3) to getString(R.string.yes),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.intermittent),
                    getString(R.string.joints_question_6) to getString(R.string.no),
                    getString(R.string.joints_question_7) to getString(R.string.yes),
                    getString(R.string.joints_question_8) to getString(R.string.no),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.no)
                ) to getString(R.string.bursitis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.multiple),
                    getString(R.string.joints_question_3) to getString(R.string.no),
                    getString(R.string.joints_question_4) to getString(R.string.no),
                    getString(R.string.joints_question_5) to getString(R.string.constant),
                    getString(R.string.joints_question_6) to getString(R.string.yes),
                    getString(R.string.joints_question_7) to getString(R.string.yes),
                    getString(R.string.joints_question_8) to getString(R.string.no),
                    getString(R.string.joints_question_9) to getString(R.string.no),
                    getString(R.string.joints_question_10) to getString(R.string.no)
                ) to getString(R.string.psoriatic_arthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.one),
                    getString(R.string.joints_question_3) to getString(R.string.no),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.intermittent),
                    getString(R.string.joints_question_6) to getString(R.string.no),
                    getString(R.string.joints_question_7) to getString(R.string.no),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.no),
                    getString(R.string.joints_question_10) to getString(R.string.no)
                ) to getString(R.string.ligament_sprain),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.multiple),
                    getString(R.string.joints_question_3) to getString(R.string.yes),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.constant),
                    getString(R.string.joints_question_6) to getString(R.string.yes),
                    getString(R.string.joints_question_7) to getString(R.string.yes),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.yes)
                ) to getString(R.string.reactive_arthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.one),
                    getString(R.string.joints_question_3) to getString(R.string.no),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.intermittent),
                    getString(R.string.joints_question_6) to getString(R.string.no),
                    getString(R.string.joints_question_7) to getString(R.string.yes),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.yes)
                ) to getString(R.string.scleroderma),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.joints_question_1) to getString(R.string.yes),
                    getString(R.string.joints_question_2) to getString(R.string.multiple),
                    getString(R.string.joints_question_3) to getString(R.string.yes),
                    getString(R.string.joints_question_4) to getString(R.string.yes),
                    getString(R.string.joints_question_5) to getString(R.string.constant),
                    getString(R.string.joints_question_6) to getString(R.string.yes),
                    getString(R.string.joints_question_7) to getString(R.string.no),
                    getString(R.string.joints_question_8) to getString(R.string.yes),
                    getString(R.string.joints_question_9) to getString(R.string.yes),
                    getString(R.string.joints_question_10) to getString(R.string.yes)
                ) to getString(R.string.systemic_lupus_erythematosus),
            ),
            getString(R.string.feet) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.one),
                    getString(R.string.feet_question_3) to getString(R.string.yes),
                    getString(R.string.feet_question_4) to getString(R.string.no),
                    getString(R.string.feet_question_5) to getString(R.string.sharp),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.yes),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.no)
                ) to getString(R.string.plantar_fasciitis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.both),
                    getString(R.string.feet_question_3) to getString(R.string.yes),
                    getString(R.string.feet_question_4) to getString(R.string.yes),
                    getString(R.string.feet_question_5) to getString(R.string.dull),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.no),
                    getString(R.string.feet_question_9) to getString(R.string.yes),
                    getString(R.string.feet_question_10) to getString(R.string.yes)
                ) to getString(R.string.peripheral_neuropathy),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.one),
                    getString(R.string.feet_question_3) to getString(R.string.no),
                    getString(R.string.feet_question_4) to getString(R.string.no),
                    getString(R.string.feet_question_5) to getString(R.string.sharp),
                    getString(R.string.feet_question_6) to getString(R.string.no),
                    getString(R.string.feet_question_7) to getString(R.string.yes),
                    getString(R.string.feet_question_8) to getString(R.string.yes),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.no)
                ) to getString(R.string.foot_sprain),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.both),
                    getString(R.string.feet_question_3) to getString(R.string.yes),
                    getString(R.string.feet_question_4) to getString(R.string.no),
                    getString(R.string.feet_question_5) to getString(R.string.dull),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.no),
                    getString(R.string.feet_question_9) to getString(R.string.yes),
                    getString(R.string.feet_question_10) to getString(R.string.yes)
                ) to getString(R.string.gout),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.one),
                    getString(R.string.feet_question_3) to getString(R.string.no),
                    getString(R.string.feet_question_4) to getString(R.string.yes),
                    getString(R.string.feet_question_5) to getString(R.string.sharp),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.no),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.no)
                ) to getString(R.string.mortons_neuroma),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.both),
                    getString(R.string.feet_question_3) to getString(R.string.no),
                    getString(R.string.feet_question_4) to getString(R.string.no),
                    getString(R.string.feet_question_5) to getString(R.string.dull),
                    getString(R.string.feet_question_6) to getString(R.string.no),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.no),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.yes)
                ) to getString(R.string.flat_feet),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.one),
                    getString(R.string.feet_question_3) to getString(R.string.yes),
                    getString(R.string.feet_question_4) to getString(R.string.no),
                    getString(R.string.feet_question_5) to getString(R.string.dull),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.yes),
                    getString(R.string.feet_question_8) to getString(R.string.yes),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.yes)
                ) to getString(R.string.tendinitis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.both),
                    getString(R.string.feet_question_3) to getString(R.string.yes),
                    getString(R.string.feet_question_4) to getString(R.string.yes),
                    getString(R.string.feet_question_5) to getString(R.string.dull),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.yes),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.yes)
                ) to getString(R.string.diabetic_foot),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.one),
                    getString(R.string.feet_question_3) to getString(R.string.no),
                    getString(R.string.feet_question_4) to getString(R.string.yes),
                    getString(R.string.feet_question_5) to getString(R.string.sharp),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.no),
                    getString(R.string.feet_question_8) to getString(R.string.no),
                    getString(R.string.feet_question_9) to getString(R.string.yes),
                    getString(R.string.feet_question_10) to getString(R.string.no)
                ) to getString(R.string.nerve_compression),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.feet_question_1) to getString(R.string.yes),
                    getString(R.string.feet_question_2) to getString(R.string.both),
                    getString(R.string.feet_question_3) to getString(R.string.yes),
                    getString(R.string.feet_question_4) to getString(R.string.no),
                    getString(R.string.feet_question_5) to getString(R.string.dull),
                    getString(R.string.feet_question_6) to getString(R.string.yes),
                    getString(R.string.feet_question_7) to getString(R.string.yes),
                    getString(R.string.feet_question_8) to getString(R.string.no),
                    getString(R.string.feet_question_9) to getString(R.string.no),
                    getString(R.string.feet_question_10) to getString(R.string.yes)
                ) to getString(R.string.arthritis)
            ),
            getString(R.string.neck) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.sharp),
                    getString(R.string.neck_question_3) to getString(R.string.yes),
                    getString(R.string.neck_question_4) to getString(R.string.yes),
                    getString(R.string.neck_question_5) to getString(R.string.no),
                    getString(R.string.neck_question_6) to getString(R.string.no),
                    getString(R.string.neck_question_7) to getString(R.string.yes),
                    getString(R.string.neck_question_8) to getString(R.string.no),
                    getString(R.string.neck_question_9) to getString(R.string.yes),
                    getString(R.string.neck_question_10) to getString(R.string.no)
                ) to getString(R.string.cervical_strain),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.dull),
                    getString(R.string.neck_question_3) to getString(R.string.no),
                    getString(R.string.neck_question_4) to getString(R.string.yes),
                    getString(R.string.neck_question_5) to getString(R.string.yes),
                    getString(R.string.neck_question_6) to getString(R.string.yes),
                    getString(R.string.neck_question_7) to getString(R.string.no),
                    getString(R.string.neck_question_8) to getString(R.string.yes),
                    getString(R.string.neck_question_9) to getString(R.string.yes),
                    getString(R.string.neck_question_10) to getString(R.string.yes)
                ) to getString(R.string.herniated_disc),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.sharp),
                    getString(R.string.neck_question_3) to getString(R.string.yes),
                    getString(R.string.neck_question_4) to getString(R.string.no),
                    getString(R.string.neck_question_5) to getString(R.string.no),
                    getString(R.string.neck_question_6) to getString(R.string.no),
                    getString(R.string.neck_question_7) to getString(R.string.no),
                    getString(R.string.neck_question_8) to getString(R.string.no),
                    getString(R.string.neck_question_9) to getString(R.string.no),
                    getString(R.string.neck_question_10) to getString(R.string.no)
                ) to getString(R.string.muscle_strain),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.dull),
                    getString(R.string.neck_question_3) to getString(R.string.no),
                    getString(R.string.neck_question_4) to getString(R.string.yes),
                    getString(R.string.neck_question_5) to getString(R.string.yes),
                    getString(R.string.neck_question_6) to getString(R.string.no),
                    getString(R.string.neck_question_7) to getString(R.string.yes),
                    getString(R.string.neck_question_8) to getString(R.string.yes),
                    getString(R.string.neck_question_9) to getString(R.string.no),
                    getString(R.string.neck_question_10) to getString(R.string.yes)
                ) to getString(R.string.spinal_stenosis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.sharp),
                    getString(R.string.neck_question_3) to getString(R.string.yes),
                    getString(R.string.neck_question_4) to getString(R.string.yes),
                    getString(R.string.neck_question_5) to getString(R.string.no),
                    getString(R.string.neck_question_6) to getString(R.string.yes),
                    getString(R.string.neck_question_7) to getString(R.string.no),
                    getString(R.string.neck_question_8) to getString(R.string.no),
                    getString(R.string.neck_question_9) to getString(R.string.yes),
                    getString(R.string.neck_question_10) to getString(R.string.no)
                ) to getString(R.string.nerve_compression),
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.dull),
                    getString(R.string.neck_question_3) to getString(R.string.no),
                    getString(R.string.neck_question_4) to getString(R.string.no),
                    getString(R.string.neck_question_5) to getString(R.string.yes),
                    getString(R.string.neck_question_6) to getString(R.string.no),
                    getString(R.string.neck_question_7) to getString(R.string.no),
                    getString(R.string.neck_question_8) to getString(R.string.yes),
                    getString(R.string.neck_question_9) to getString(R.string.no),
                    getString(R.string.neck_question_10) to getString(R.string.yes)
                ) to getString(R.string.osteoarthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.neck_question_1) to getString(R.string.yes),
                    getString(R.string.neck_question_2) to getString(R.string.sharp),
                    getString(R.string.neck_question_3) to getString(R.string.yes),
                    getString(R.string.neck_question_4) to getString(R.string.yes),
                    getString(R.string.neck_question_5) to getString(R.string.no),
                    getString(R.string.neck_question_6) to getString(R.string.yes),
                    getString(R.string.neck_question_7) to getString(R.string.no),
                    getString(R.string.neck_question_8) to getString(R.string.no),
                    getString(R.string.neck_question_9) to getString(R.string.yes),
                    getString(R.string.neck_question_10) to getString(R.string.no)
                ) to getString(R.string.rheumatoid_arthritis)
            ),
            getString(R.string.hands) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.hands_question_1) to getString(R.string.yes),
                    getString(R.string.hands_question_2) to getString(R.string.one),
                    getString(R.string.hands_question_3) to getString(R.string.yes),
                    getString(R.string.hands_question_4) to getString(R.string.no),
                    getString(R.string.hands_question_5) to getString(R.string.sharp),
                    getString(R.string.hands_question_6) to getString(R.string.yes),
                    getString(R.string.hands_question_7) to getString(R.string.no),
                    getString(R.string.hands_question_8) to getString(R.string.yes),
                    getString(R.string.hands_question_9) to getString(R.string.no),
                    getString(R.string.hands_question_10) to getString(R.string.no)
                ) to getString(R.string.carpal_tunnel_syndrome),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.hands_question_1) to getString(R.string.yes),
                    getString(R.string.hands_question_2) to getString(R.string.both),
                    getString(R.string.hands_question_3) to getString(R.string.no),
                    getString(R.string.hands_question_4) to getString(R.string.yes),
                    getString(R.string.hands_question_5) to getString(R.string.dull),
                    getString(R.string.hands_question_6) to getString(R.string.no),
                    getString(R.string.hands_question_7) to getString(R.string.yes),
                    getString(R.string.hands_question_8) to getString(R.string.no),
                    getString(R.string.hands_question_9) to getString(R.string.yes),
                    getString(R.string.hands_question_10) to getString(R.string.yes)
                ) to getString(R.string.rheumatoid_arthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.hands_question_1) to getString(R.string.yes),
                    getString(R.string.hands_question_2) to getString(R.string.one),
                    getString(R.string.hands_question_3) to getString(R.string.yes),
                    getString(R.string.hands_question_4) to getString(R.string.yes),
                    getString(R.string.hands_question_5) to getString(R.string.sharp),
                    getString(R.string.hands_question_6) to getString(R.string.yes),
                    getString(R.string.hands_question_7) to getString(R.string.no),
                    getString(R.string.hands_question_8) to getString(R.string.yes),
                    getString(R.string.hands_question_9) to getString(R.string.no),
                    getString(R.string.hands_question_10) to getString(R.string.no)
                ) to getString(R.string.gout),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.hands_question_1) to getString(R.string.yes),
                    getString(R.string.hands_question_2) to getString(R.string.both),
                    getString(R.string.hands_question_3) to getString(R.string.no),
                    getString(R.string.hands_question_4) to getString(R.string.no),
                    getString(R.string.hands_question_5) to getString(R.string.dull),
                    getString(R.string.hands_question_6) to getString(R.string.no),
                    getString(R.string.hands_question_7) to getString(R.string.yes),
                    getString(R.string.hands_question_8) to getString(R.string.no),
                    getString(R.string.hands_question_9) to getString(R.string.yes),
                    getString(R.string.hands_question_10) to getString(R.string.yes)
                ) to getString(R.string.tendonitis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.hands_question_1) to getString(R.string.yes),
                    getString(R.string.hands_question_2) to getString(R.string.one),
                    getString(R.string.hands_question_3) to getString(R.string.yes),
                    getString(R.string.hands_question_4) to getString(R.string.no),
                    getString(R.string.hands_question_5) to getString(R.string.sharp),
                    getString(R.string.hands_question_6) to getString(R.string.yes),
                    getString(R.string.hands_question_7) to getString(R.string.no),
                    getString(R.string.hands_question_8) to getString(R.string.yes),
                    getString(R.string.hands_question_9) to getString(R.string.yes),
                    getString(R.string.hands_question_10) to getString(R.string.no)
                ) to getString(R.string.osteoarthritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.hands_question_1) to getString(R.string.yes),
                    getString(R.string.hands_question_2) to getString(R.string.both),
                    getString(R.string.hands_question_3) to getString(R.string.yes),
                    getString(R.string.hands_question_4) to getString(R.string.yes),
                    getString(R.string.hands_question_5) to getString(R.string.sharp),
                    getString(R.string.hands_question_6) to getString(R.string.yes),
                    getString(R.string.hands_question_7) to getString(R.string.yes),
                    getString(R.string.hands_question_8) to getString(R.string.yes),
                    getString(R.string.hands_question_9) to getString(R.string.no),
                    getString(R.string.hands_question_10) to getString(R.string.no)
                ) to getString(R.string.de_quervains_tenosynovitis)
            ),
            getString(R.string.stomach) to listOf(
                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.stomach_question_1) to getString(R.string.yes),
                    getString(R.string.stomach_question_2) to getString(R.string.sharp),
                    getString(R.string.stomach_question_3) to getString(R.string.yes),
                    getString(R.string.stomach_question_4) to getString(R.string.constant),
                    getString(R.string.stomach_question_5) to getString(R.string.no),
                    getString(R.string.stomach_question_6) to getString(R.string.no),
                    getString(R.string.stomach_question_7) to getString(R.string.yes),
                    getString(R.string.stomach_question_8) to getString(R.string.no),
                    getString(R.string.stomach_question_9) to getString(R.string.yes),
                    getString(R.string.stomach_question_10) to getString(R.string.no)
                ) to getString(R.string.gastritis),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.stomach_question_1) to getString(R.string.yes),
                    getString(R.string.stomach_question_2) to getString(R.string.cramping),
                    getString(R.string.stomach_question_3) to getString(R.string.no),
                    getString(R.string.stomach_question_4) to getString(R.string.intermittent),
                    getString(R.string.stomach_question_5) to getString(R.string.yes),
                    getString(R.string.stomach_question_6) to getString(R.string.yes),
                    getString(R.string.stomach_question_7) to getString(R.string.yes),
                    getString(R.string.stomach_question_8) to getString(R.string.yes),
                    getString(R.string.stomach_question_9) to getString(R.string.no),
                    getString(R.string.stomach_question_10) to getString(R.string.yes)
                ) to getString(R.string.irritable_bowel_syndrome),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.one_to_three_days),
                    getString(R.string.stomach_question_1) to getString(R.string.yes),
                    getString(R.string.stomach_question_2) to getString(R.string.sharp),
                    getString(R.string.stomach_question_3) to getString(R.string.yes),
                    getString(R.string.stomach_question_4) to getString(R.string.constant),
                    getString(R.string.stomach_question_5) to getString(R.string.no),
                    getString(R.string.stomach_question_6) to getString(R.string.no),
                    getString(R.string.stomach_question_7) to getString(R.string.no),
                    getString(R.string.stomach_question_8) to getString(R.string.no),
                    getString(R.string.stomach_question_9) to getString(R.string.yes),
                    getString(R.string.stomach_question_10) to getString(R.string.no)
                ) to getString(R.string.peptic_ulcer),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.more_than_three_days),
                    getString(R.string.stomach_question_1) to getString(R.string.yes),
                    getString(R.string.stomach_question_2) to getString(R.string.cramping),
                    getString(R.string.stomach_question_3) to getString(R.string.no),
                    getString(R.string.stomach_question_4) to getString(R.string.intermittent),
                    getString(R.string.stomach_question_5) to getString(R.string.no),
                    getString(R.string.stomach_question_6) to getString(R.string.yes),
                    getString(R.string.stomach_question_7) to getString(R.string.yes),
                    getString(R.string.stomach_question_8) to getString(R.string.yes),
                    getString(R.string.stomach_question_9) to getString(R.string.no),
                    getString(R.string.stomach_question_10) to getString(R.string.yes)
                ) to getString(R.string.constipation),

                listOf(
                    getString(R.string.when_symptoms_happen) to getString(R.string.less_than_24_hours),
                    getString(R.string.stomach_question_1) to getString(R.string.yes),
                    getString(R.string.stomach_question_2) to getString(R.string.cramping),
                    getString(R.string.stomach_question_3) to getString(R.string.no),
                    getString(R.string.stomach_question_4) to getString(R.string.constant),
                    getString(R.string.stomach_question_5) to getString(R.string.yes),
                    getString(R.string.stomach_question_6) to getString(R.string.no),
                    getString(R.string.stomach_question_7) to getString(R.string.yes),
                    getString(R.string.stomach_question_8) to getString(R.string.no),
                    getString(R.string.stomach_question_9) to getString(R.string.yes),
                    getString(R.string.stomach_question_10) to getString(R.string.no)
                ) to getString(R.string.gastroenteritis)
            )
        )

        val possibleConditions = diagnosisOptions[painLocation]?.map { (criteria, condition) ->
            val matchingCriteria = criteria.count { (question, answer) ->
                answersSelected[question] == answer
            }

            // Get the answer to "When did the symptoms happen?"
            val symptomsTiming = answersSelected[getString(R.string.when_symptoms_happen)]

            // Calculate base confidence percentage
            var confidencePercentage = (matchingCriteria.toDouble() / criteria.size * 70).toInt() // Base confidence is 0% - 70%

            // Add timing influence (max 30%)
            when (symptomsTiming) {
                getString(R.string.less_than_24_hours),
                getString(R.string.one_to_three_days),
                getString(R.string.more_than_three_days) -> {
                    confidencePercentage += 30 // Increase confidence by 30% for valid timing
                }
            }

            // Ensure confidence percentage does not exceed 100%
            confidencePercentage = confidencePercentage.coerceAtMost(100)

            // Ensure confidence percentage does not drop below zero
            confidencePercentage = confidencePercentage.coerceAtLeast(0)

            condition to confidencePercentage
        } ?: emptyList()

        // Sort the possible conditions by confidence percentage in descending order
        val topConditions = possibleConditions.sortedByDescending { it.second }.take(3)

        val resultString = if (topConditions.isNotEmpty()) {
            // Start with the "Possible Condition" label, followed by two newlines for spacing
            val header = "${getString(R.string.possible_condition)}\n\n" // Add extra spacing after header

            // Join conditions, placing confidence on the next line
            val conditionsList = topConditions.joinToString("\n\n") {
                "${it.first}\n(${getString(R.string.confidence)} ${it.second}%)" // Confidence on a new line
            }

            // Combine header and conditions
            "$header$conditionsList"
        } else {
            "${getString(R.string.unknown_condition)} (Confidence 0%)" // Return a single string
        }

        return resultString

    }

    private fun showAlert() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom, null)
        val messageTextView: TextView = dialogView.findViewById(R.id.dialog_message)
        val okButton: Button = dialogView.findViewById(R.id.dialog_button)

        messageTextView.text = getString(R.string.advisory_message)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        okButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
