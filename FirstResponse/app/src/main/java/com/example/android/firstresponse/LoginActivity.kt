package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

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

                btnLogin.setOnClickListener {
                        val username = etUsername.text.toString().trim()
                        val password = etPassword.text.toString().trim()

                        if (username.isEmpty() || password.isEmpty()) {
                                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                        }

                        // Query database to find email for the provided username
                        val database = FirebaseDatabase.getInstance()
                        val usersRef = database.getReference("users")

                        usersRef.orderByChild("username").equalTo(username)
                                .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                                        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                                                if (snapshot.exists()) {
                                                        for (userSnapshot in snapshot.children) {
                                                                val email = userSnapshot.child("email").value.toString()

                                                                // Use FirebaseAuth to log in with email and password
                                                                auth.signInWithEmailAndPassword(email, password)
                                                                        .addOnCompleteListener { task ->
                                                                                if (task.isSuccessful) {
                                                                                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                                                                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                                                        startActivity(intent)
                                                                                        finish()
                                                                                } else {
                                                                                        Toast.makeText(
                                                                                                this@LoginActivity,
                                                                                                "Login failed: ${task.exception?.message}",
                                                                                                Toast.LENGTH_SHORT
                                                                                        ).show()
                                                                                }
                                                                        }
                                                                break
                                                        }
                                                } else {
                                                        Toast.makeText(this@LoginActivity, "Username not found", Toast.LENGTH_SHORT).show()
                                                }
                                        }

                                        override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                                                Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                                        }
                                })
                }

                btnGoToRegister.setOnClickListener {
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                }
        }
}
