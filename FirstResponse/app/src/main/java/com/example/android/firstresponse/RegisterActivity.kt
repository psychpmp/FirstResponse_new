package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executor

class RegisterActivity : AppCompatActivity() {

        private lateinit var etUsername: EditText
        private lateinit var etPassword: EditText
        private lateinit var etEmail: EditText
        private lateinit var etFullname: EditText
        private lateinit var etAddress: EditText
        private lateinit var etBirthday: EditText
        private lateinit var btnRegister: ImageButton
        private lateinit var btnGoToLogin: Button
        private lateinit var cbFingerprint: CheckBox
        private lateinit var btnSendOtp: Button

        private lateinit var auth: FirebaseAuth
        private lateinit var database: DatabaseReference

        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var executor: Executor
        private var isEmailVerified = false

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_register)

                // Initialize views
                initViews()


                auth = FirebaseAuth.getInstance()
                database = FirebaseDatabase.getInstance().reference

                executor = ContextCompat.getMainExecutor(this)
                biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                registerUser()
                        }
                })

                // Button click listeners
                btnSendOtp.setOnClickListener { sendOtpToEmail() }

                btnRegister.setOnClickListener {  // Use ImageButton
                        animateButton(btnRegister)
                        btnRegister.postDelayed({
                                if (validateInputs()) {
                                        if (!isEmailVerified) {
                                                Toast.makeText(this, "Please verify your email before registering.", Toast.LENGTH_SHORT).show()
                                                return@postDelayed
                                        }

                                        if (!cbFingerprint.isChecked) {
                                                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                                        .setTitle("Fingerprint Authentication")
                                                        .setSubtitle("Authenticate to register")
                                                        .setNegativeButtonText("Cancel")
                                                        .build()
                                                biometricPrompt.authenticate(promptInfo)
                                        } else {
                                                registerUser()
                                        }
                                }
                        }, 200) // Delay to allow button animations to finish
                }

                btnGoToLogin.setOnClickListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                }
        }

        private fun initViews() {
                etUsername = findViewById(R.id.et_register_username)
                etPassword = findViewById(R.id.et_register_password)
                etEmail = findViewById(R.id.et_register_email)
                etFullname = findViewById(R.id.et_register_fullname)
                etAddress = findViewById(R.id.et_register_address)
                etBirthday = findViewById(R.id.et_register_birthday)
                btnRegister = findViewById(R.id.btn_register)  // Initialize as ImageButton
                btnGoToLogin = findViewById(R.id.btn_go_to_login)
                cbFingerprint = findViewById(R.id.cb_fingerprint)
                btnSendOtp = findViewById(R.id.btn_send_otp)
        }

        override fun onResume() {
                super.onResume()
                checkEmailVerificationStatus()
        }

        private fun sendOtpToEmail() {
                val email = etEmail.text.toString().trim()
                if (email.isEmpty()) {
                        Toast.makeText(this, "Please enter an email address.", Toast.LENGTH_SHORT).show()
                        return
                }

                auth.createUserWithEmailAndPassword(email, "TemporaryPassword123")
                        .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                        val user = auth.currentUser
                                        user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                                                if (emailTask.isSuccessful) {
                                                        Toast.makeText(this, "Verification sent to $email. Verify to proceed.", Toast.LENGTH_SHORT).show()
                                                        checkEmailVerificationStatus()
                                                } else {
                                                        Toast.makeText(this, "Failed to send verification email: ${emailTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                } else {
                                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
        }

        private fun checkEmailVerificationStatus() {
                val currentUser = auth.currentUser
                currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                                if (currentUser.isEmailVerified) {
                                        isEmailVerified = true
                                        updateEmailVerifiedUI()
                                } else {
                                        Toast.makeText(this, "Please verify your email to proceed.", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                                Toast.makeText(this, "Failed to reload user data: ${reloadTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                }
        }

        private fun updateEmailVerifiedUI() {
                btnSendOtp.text = "Email Verified"
                btnSendOtp.isEnabled = false
                btnSendOtp.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVerified))
                Toast.makeText(this, "Email Verified. Proceeding with registration.", Toast.LENGTH_SHORT).show()
        }

        private fun animateButton(button: ImageButton) {
                button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_press))
                button.postDelayed({
                        button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_release))
                }, 100)
        }

        private fun validateInputs(): Boolean {
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val fullname = etFullname.text.toString().trim()
                val address = etAddress.text.toString().trim()
                val birthday = etBirthday.text.toString().trim()

                return when {
                        username.isEmpty() -> {
                                etUsername.error = "Username is required"
                                false
                        }
                        password.isEmpty() -> {
                                etPassword.error = "Password is required"
                                false
                        }
                        email.isEmpty() -> {
                                etEmail.error = "Email is required"
                                false
                        }
                        fullname.isEmpty() -> {
                                etFullname.error = "Full name is required"
                                false
                        }
                        address.isEmpty() -> {
                                etAddress.error = "Address is required"
                                false
                        }
                        birthday.isEmpty() -> {
                                etBirthday.error = "Birthday is required"
                                false
                        }
                        else -> true
                }
        }

        private fun registerUser() {
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val fullname = etFullname.text.toString().trim()
                val address = etAddress.text.toString().trim()
                val birthday = etBirthday.text.toString().trim()

                // Check current user
                val currentUser = auth.currentUser
                if (currentUser == null) {
                        Toast.makeText(this, "Error: No authenticated user found.", Toast.LENGTH_SHORT).show()
                        return
                }

                currentUser.reload().addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful && currentUser.isEmailVerified) {
                                val userId = currentUser.uid

                                // Create a User object
                                val user = User(
                                        username = username,
                                        email = email,
                                        fullname = fullname,
                                        address = address,
                                        birthday = birthday,
                                        fingerprintRegistered = cbFingerprint.isChecked
                                )

                                // Store the User object in the database
                                database.child("users").child(userId).setValue(user)
                                        .addOnSuccessListener {
                                                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(this, LoginActivity::class.java))
                                                finish()
                                        }
                                        .addOnFailureListener { exception ->
                                                Toast.makeText(this, "Failed to register user: ${exception.message}", Toast.LENGTH_SHORT).show()
                                        }
                        } else {
                                Toast.makeText(this, "Please verify your email before proceeding.", Toast.LENGTH_SHORT).show()
                        }
                }
        }
}

data class User(
        val username: String = "",
        val email: String = "",
        val fullname: String = "",
        val address: String = "",
        val birthday: String = "",
        val fingerprintRegistered: Boolean = false
)