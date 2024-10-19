package com.example.samsara.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsData (context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_MODE = "mode"
        private const val DEFAULT_LANGUAGE = "en"
        private const val DEFAULT_MODE = "light"
    }

    var language: String
        get() = sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
        set(value) {
            sharedPreferences.edit().putString(KEY_LANGUAGE, value).apply()
        }

    var mode: String
        get() = sharedPreferences.getString(KEY_MODE, DEFAULT_MODE) ?: DEFAULT_MODE
        set(value) {
            sharedPreferences.edit().putString(KEY_MODE, value).apply()
        }

    fun clearSettings() {
        sharedPreferences.edit().clear().apply()
    }
}