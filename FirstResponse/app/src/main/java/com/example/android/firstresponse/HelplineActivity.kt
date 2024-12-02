package com.example.android.firstresponse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

class HelplineActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpline)

        // Hide the Action Bar for this activity
        supportActionBar?.hide()

        // Initialize LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView?.apply {
            setAnimation(R.raw.emerg) // Ensure this matches your animation file
            playAnimation()
            repeatCount = LottieDrawable.INFINITE // Optional: loop animation
        }

        // Button for POLANGUI EMS QRT
        val buttonPOLANGUIEMS: Button = findViewById(R.id.buttonPOLANGUIEMS)
        buttonPOLANGUIEMS.setOnClickListener {
            makeCall("09277555625") // Replace with actual phone number
        }


        // Initialize buttons and set their click listeners
        val buttonMDRRMOPOL: Button = findViewById(R.id.buttonMDRRMOPOL)
        buttonMDRRMOPOL.setOnClickListener {
            makeCall("09610512959") // Replace with actual phone number
        }

        val buttonPolanguiRHU: Button = findViewById(R.id.button_polangui_rhu)
        buttonPolanguiRHU.setOnClickListener {
            makeCall("09152625978") // Replace with actual phone number
        }

        // Button for OAS Emergency Response Team
        val buttonOAS: Button = findViewById(R.id.buttonOAS)
        buttonOAS.setOnClickListener {
            makeCall("09205871939") // Replace with actual phone number
        }

        val buttonMDRRMOOAS: Button = findViewById(R.id.button_mdrmmo_oas)
        buttonMDRRMOOAS.setOnClickListener {
            makeCall("09289800909") // Replace with actual phone number
        }

        val buttonOASRHU: Button = findViewById(R.id.button_oas_rhu)
        buttonOASRHU.setOnClickListener {
            makeCall("09509007041") // Replace with actual phone number
        }

        // Button for CDRRMO EQRT LIGAO CITY
        val buttonCDRRMO: Button = findViewById(R.id.buttonCDRRMO)
        buttonCDRRMO.setOnClickListener {
            makeCall("09954004711") // Replace with actual phone number
        }

        val buttonZoneMedical: Button = findViewById(R.id.button_zone_medical)
        buttonZoneMedical.setOnClickListener {
            makeCall("09156985788") // Replace with actual phone number
        }

        val buttonLigaoRhu: Button = findViewById(R.id.buttonLigaoRhu)
        buttonLigaoRhu.setOnClickListener {
            makeCall("09051985920") // Replace with actual phone number
        }

        val buttonDuran: Button = findViewById(R.id.buttonduranbel)
        buttonDuran.setOnClickListener {
            makeCall("09452962595") // Replace with actual phone number
        }



        // Initialize BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottomHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    // Apply transition animations
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.bottomSaved -> {
                    val intent = Intent(this, SavedActivity::class.java)
                    startActivity(intent)
                    // Apply transition animations
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.bottomHelpline -> {
                    // Already in HelplineActivity, no need to navigate
                    true
                }
                else -> false
            }
        }

        // Set the Helpline item as selected
        bottomNavigationView.menu.findItem(R.id.bottomHelpline).isChecked = true

        // Initialize Question Icon Button
        val questionIcon: ImageButton = findViewById(R.id.questionIcon)
        questionIcon.setOnClickListener {
            showInstructionsDialog()
        }
    }

    private fun showInstructionsDialog() {
        // Inflate the custom layout for the dialog
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_instructions, null)

        // Find the views in the dialog
        val dialogImage: ImageView = dialogView.findViewById(R.id.dialog_image)
        val dialogMessage: TextView = dialogView.findViewById(R.id.dialog_message)
        val dialogButton: Button = dialogView.findViewById(R.id.dialog_button)

        // Set the title and message for the dialog
        dialogMessage.text = getString(R.string.instruction_dialog_message)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.instruction_dialog_title)) // Set the title
        dialogBuilder.setView(dialogView)

        // Create the dialog
        val dialog = dialogBuilder.create()

        // Set the button click listener to dismiss the dialog
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }


    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }
}