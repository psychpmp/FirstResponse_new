package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executor

class RegisterActivity : AppCompatActivity() {

        private lateinit var etFullName: EditText
        private lateinit var etAge: EditText
        private lateinit var etAddress: EditText
        private lateinit var btnUploadPhoto: Button
        private lateinit var etUsername: EditText
        private lateinit var etPassword: EditText
        private lateinit var etEmail: EditText
        private lateinit var btnRegister: ImageButton
        private lateinit var btnGoToLogin: Button
        private lateinit var cbFingerprint: CheckBox

        private lateinit var auth: FirebaseAuth
        private lateinit var database: DatabaseReference

        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var executor: Executor

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_register)

                // Bind views
                etFullName = findViewById(R.id.et_register_fullname)
                etAge = findViewById(R.id.et_register_age)
                etAddress = findViewById(R.id.et_register_address)
                btnUploadPhoto = findViewById(R.id.btn_upload_photo)
                etUsername = findViewById(R.id.et_register_username)
                etPassword = findViewById(R.id.et_register_password)
                etEmail = findViewById(R.id.et_register_email)
                btnRegister = findViewById(R.id.btn_register)
                btnGoToLogin = findViewById(R.id.btn_go_to_login)
                cbFingerprint = findViewById(R.id.cb_fingerprint)

                auth = FirebaseAuth.getInstance()
                database = FirebaseDatabase.getInstance().reference

                // Handle photo upload
                btnUploadPhoto.setOnClickListener {
                        Toast.makeText(this, "Photo upload feature coming soon!", Toast.LENGTH_SHORT).show()
                }

                btnRegister.setOnClickListener {
                        val fullName = etFullName.text.toString().trim()
                        val age = etAge.text.toString().trim()
                        val address = etAddress.text.toString().trim()
                        val username = etUsername.text.toString().trim()
                        val password = etPassword.text.toString().trim()
                        val email = etEmail.text.toString().trim()

                        // Validate inputs
                        if (fullName.isEmpty() || age.isEmpty() || address.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                        }

                        // Call registerUser() or perform biometric check if necessary
                        registerUser(fullName, age, address, username, email)
                }

                btnGoToLogin.setOnClickListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                }
        }

        private fun registerUser(fullName: String, age: String, address: String, username: String, email: String) {
                auth.createUserWithEmailAndPassword(email, etPassword.text.toString().trim())
                        .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                        val userId = auth.currentUser?.uid
                                        val userMap = mapOf(
                                                "fullname" to fullName,
                                                "age" to age,
                                                "address" to address,
                                                "username" to username,
                                                "email" to email
                                        )
                                        userId?.let {
                                                database.child("users").child(it).setValue(userMap)
                                                        .addOnSuccessListener {
                                                                Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
                                                                val intent = Intent(this, LoginActivity::class.java)
                                                                startActivity(intent)
                                                                finish()
                                                        }
                                                        .addOnFailureListener { e ->
                                                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                                        }
                                        }
                                } else {
                                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
        }
}
