package com.example.android.firstresponse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class LanguageChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Log when the language change is received
        Log.d("LanguageChangeReceiver", "Language change broadcast received.")
        // Restart the activity to apply new language
        if (context is BaseActivity) {
            context.recreate() // Recreate activity to apply new language
        }
    }
}
