package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserInfoActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var tvFullName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvBirthday: TextView
    private lateinit var btnLogout: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Check if the user is logged in and email-verified
        val currentUser = auth.currentUser
        if (currentUser == null || !currentUser.isEmailVerified) {
            // Redirect to the login activity if not logged in or email not verified
            Toast.makeText(this, "Please log in and verify your email.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Bind views
        tvUsername = findViewById(R.id.tvUsername)
        tvFullName = findViewById(R.id.tvFullName)
        tvEmail = findViewById(R.id.tvEmail)
        tvAddress = findViewById(R.id.tvAddress)
        tvBirthday = findViewById(R.id.tvBirthday)
        btnLogout = findViewById(R.id.btnLogout)

        // Fetch the current user ID
        val currentUserId = currentUser.uid

        // Fetch user data from Firebase
        database.child("users").child(currentUserId).get().addOnSuccessListener { snapshot ->
            // Check if data exists for the user
            if (snapshot.exists()) {
                val username = snapshot.child("username").value?.toString() ?: "N/A"
                val fullName = snapshot.child("fullname").value?.toString() ?: "N/A"
                val email = snapshot.child("email").value?.toString() ?: "N/A"
                val address = snapshot.child("address").value?.toString() ?: "N/A"
                val birthday = snapshot.child("birthday").value?.toString() ?: "N/A"

                // Display the user data
                tvUsername.text = "Username: $username"
                tvFullName.text = "Full Name: $fullName"
                tvEmail.text = "Email: $email"
                tvAddress.text = "Address: $address"
                tvBirthday.text = "Birthday: $birthday"
            } else {
                // Handle case where data doesn't exist for the user
                tvUsername.text = "User data not found"
            }
        }.addOnFailureListener { exception ->
            // Handle errors (e.g., network failure, no data)
            tvUsername.text = "Failed to load data: ${exception.message}"
        }

        // Set up the logout button
        btnLogout.setOnClickListener {
            // Sign out from Firebase
            auth.signOut()

            // Show a toast message
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Redirect to the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Optional, to close the current activity
        }
    }
}
