package com.example.android.firstresponse;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

private lateinit var etUsername: EditText
private lateinit var etPassword: EditText
private lateinit var etEmail: EditText
private lateinit var btnRegister: ImageButton
private lateinit var btnGoToLogin: Button

private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.et_register_username)
        etPassword = findViewById(R.id.et_register_password)
        etEmail = findViewById(R.id.et_register_email)
        btnRegister = findViewById(R.id.btn_register)
        btnGoToLogin = findViewById(R.id.btn_go_to_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        btnRegister.setOnClickListener {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
            }

                    auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userMap = mapOf(
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

        btnGoToLogin.setOnClickListener {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        }
        }
        }
