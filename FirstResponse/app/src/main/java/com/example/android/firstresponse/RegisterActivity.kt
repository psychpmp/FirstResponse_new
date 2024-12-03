package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

        private lateinit var etUsername: EditText
        private lateinit var etEmail: EditText
        private lateinit var etPassword: EditText
        private lateinit var btnRegister: Button
        private lateinit var btnGoToLogin: Button
        private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_register)

                // Initialize views
                etUsername = findViewById(R.id.et_register_username)
                etEmail = findViewById(R.id.et_register_email)
                etPassword = findViewById(R.id.et_register_password)
                btnRegister = findViewById(R.id.btn_register)
                btnGoToLogin = findViewById(R.id.btn_go_to_login)

                // Initialize FirebaseAuth
                auth = FirebaseAuth.getInstance()

                btnRegister.setOnClickListener {
                        val username = etUsername.text.toString().trim()
                        val email = etEmail.text.toString().trim()
                        val password = etPassword.text.toString().trim()

                        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                        }

                        // Register user with FirebaseAuth
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                                val userId = auth.currentUser?.uid
                                                if (userId != null) {
                                                        val database = FirebaseDatabase.getInstance()
                                                        val usersRef = database.getReference("users")

                                                        val user = mapOf(
                                                                "username" to username,
                                                                "email" to email
                                                        )
                                                        usersRef.child(userId).setValue(user)
                                                                .addOnCompleteListener { saveTask ->
                                                                        if (saveTask.isSuccessful) {
                                                                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                                                                val intent = Intent(this, LoginActivity::class.java)
                                                                                startActivity(intent)
                                                                                finish()
                                                                        } else {
                                                                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                }
                                                }
                                        } else {
                                                Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                }

                btnGoToLogin.setOnClickListener {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                }
        }
}
