package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

        // Declare views and FirebaseAuth instance
        private lateinit var etUsername: EditText
        private lateinit var etPassword: EditText
        private lateinit var btnLogin: ImageButton
        private lateinit var btnGoToRegister: Button
        private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_login)

                // Initialize views
                etUsername = findViewById(R.id.et_username)
                etPassword = findViewById(R.id.et_password)
                btnLogin = findViewById(R.id.btn_login)
                btnGoToRegister = findViewById(R.id.btn_go_to_register)

                // Initialize FirebaseAuth
                auth = FirebaseAuth.getInstance()

                // Set login button click listener
                btnLogin.setOnClickListener {
                        val email = etUsername.text.toString().trim()
                        val password = etPassword.text.toString().trim()

                        if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                        }

                        // Attempt Firebase login
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                                // Navigate to DashboardActivity
                                                val intent = Intent(this, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                        } else {
                                                Toast.makeText(
                                                        this,
                                                        "Login failed: ${task.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                ).show()
                                        }
                                }
                }

                // Set register button click listener
                btnGoToRegister.setOnClickListener {
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                }
        }
}
