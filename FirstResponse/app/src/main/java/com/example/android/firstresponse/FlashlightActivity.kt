package com.example.android.firstresponse

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast

class FlashlightActivity : BaseActivity() {
    private lateinit var flashlightManager: FlashlightManager
    private var mediaPlayer: MediaPlayer? = null // MediaPlayer for alarm sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashlight)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = "FLASHLIGHT"  // Replace with your desired title
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

        flashlightManager = FlashlightManager(this)

        // Request Camera permission if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        // Set up buttons for flashlight modes
        findViewById<Button>(R.id.btn_flashlight_on).setOnClickListener {
            if (isCameraPermissionGranted()) {
                flashlightManager.turnOn()
            } else {
                showPermissionRequiredToast()
            }
        }

        findViewById<Button>(R.id.btn_flashlight_off).setOnClickListener {
            flashlightManager.turnOff()
        }

        findViewById<Button>(R.id.btn_flashlight_strobe).setOnClickListener {
            if (isCameraPermissionGranted()) {
                flashlightManager.strobe(500) // Adjust frequency as needed
            } else {
                showPermissionRequiredToast()
            }
        }

        findViewById<Button>(R.id.btn_flashlight_sos).setOnClickListener {
            if (isCameraPermissionGranted()) {
                flashlightManager.sos()
            } else {
                showPermissionRequiredToast()
            }
        }

        // New Button for SOS with Alarm
        findViewById<Button>(R.id.btn_flashlight_alarm).setOnClickListener {
            if (isCameraPermissionGranted()) {
                flashlightManager.sosWithAlarm() // Start SOS with alarm sound
            } else {
                showPermissionRequiredToast()
            }
        }

        findViewById<Button>(R.id.btn_flashlight_stop).setOnClickListener {
            flashlightManager.stopFlashing()
            stopAlarm() // Stop alarm when stopping the flashlight
        }

        // Show instructions to the user
        showInstructions()
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionRequiredToast() {
        Toast.makeText(this, "Camera permission is required to use the flashlight.", Toast.LENGTH_SHORT).show()
    }

    private fun showInstructions() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_flashlight_instructions, null)

        // Find the TextView for the message
        val dialogMessage: TextView = dialogView.findViewById(R.id.dialog_message)

        // Set the title and message for the dialog
        val instructionTitle = getString(R.string.dialog_title_flashlight) // Assuming you create this string resource
        dialogMessage.text = getString(R.string.dialog_message) // Your existing message

        // Create the dialog
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(instructionTitle) // Set the instruction title
            .setView(dialogView) // Use the custom dialog layout
            .setCancelable(true)
            .create() // Create the dialog without showing it yet

        // Find the OK button and set a click listener
        val dialogButton = dialogView.findViewById<Button>(R.id.dialog_button)
        dialogButton.setOnClickListener {
            alertDialog.dismiss() // Dismiss the dialog when the button is clicked
        }

        alertDialog.show() // Now show the dialog
    }


    // Function to stop the alarm sound
    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        flashlightManager.stopFlashing() // Ensure the flashlight is turned off when the activity is destroyed
        stopAlarm() // Stop alarm when activity is destroyed
    }
}
