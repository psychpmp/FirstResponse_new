package com.example.android.firstresponse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SuggestionsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)

        val suggestionButton: Button = findViewById(R.id.btn_suggestion)
        suggestionButton.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:firstreponse@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Suggestion")
            putExtra(Intent.EXTRA_TEXT, "Your suggestion goes here...")
        }

        // Check if there is an email client available
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(emailIntent)
        }
    }
}
