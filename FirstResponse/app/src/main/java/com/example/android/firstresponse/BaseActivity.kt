package com.example.android.firstresponse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    private lateinit var languageChangeReceiver: BroadcastReceiver

    override fun attachBaseContext(newBase: Context) {
        val newLocaleContext = applyLocale(newBase)  // Apply the locale to the base context
        super.attachBaseContext(newLocaleContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the broadcast receiver to listen for language changes
        languageChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                recreate()  // Recreate the activity to apply the new language immediately
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(languageChangeReceiver, IntentFilter("LANGUAGE_CHANGED"))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver to prevent memory leaks
        LocalBroadcastManager.getInstance(this).unregisterReceiver(languageChangeReceiver)
    }

    private fun applyLocale(context: Context): Context {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"
        val locale = Locale(savedLanguage)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Return a new context with the updated locale
        return context.createConfigurationContext(config)
    }
}
