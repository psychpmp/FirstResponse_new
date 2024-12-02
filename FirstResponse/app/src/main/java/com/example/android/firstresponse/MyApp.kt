package com.example.android.firstresponse

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        applySavedLocale()
    }

    fun applySavedLocale() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"
        updateLocale(savedLanguage)
    }

    fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
