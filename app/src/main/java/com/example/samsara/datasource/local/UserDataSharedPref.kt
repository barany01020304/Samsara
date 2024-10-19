package com.example.samsara.datasource.local

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class UserDataSharedPref(context: Context) {
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(
        MasterKey.KeyScheme.AES256_GCM).build()
    private val sharedPreferences = EncryptedSharedPreferences.create(context, "secret_user_data", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    // Shared Preferences and Editor
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREFS_NAME = "user_prefs"

        // Keys for storing data
        private const val KEY_IMAGE_URI = "key_image_uri"
        private const val KEY_NAME = "key_name"
        private const val KEY_PHONE_NUMBER = "key_phone_number"
        private const val KEY_LOCATION = "key_location"
        private const val KEY_LATITUDE = "key_latitude"
        private const val KEY_LONGITUDE = "key_longitude"
    }

    // Save the image URI
    fun setImageUri(uri: Uri) {
        editor.putString(KEY_IMAGE_URI, uri.toString()).apply()
    }

    // Save the user name
    fun setUserName(name: String) {
        editor.putString(KEY_NAME, name).apply()
    }

    // Save the phone number
    fun setPhoneNumber(phoneNumber: String) {
        editor.putString(KEY_PHONE_NUMBER, phoneNumber).apply()
    }

    // Save the location name (e.g., city)
    fun setLocation(location: String) {
        editor.putString(KEY_LOCATION, location).apply()
    }

    // Save latitude and longitude
    fun setCoordinates(latitude: Double, longitude: Double) {
        editor.putFloat(KEY_LATITUDE, latitude.toFloat()).apply()
        editor.putFloat(KEY_LONGITUDE, longitude.toFloat()).apply()
    }

    // Retrieve the image URI
    fun getImageUri(): Uri? {
        val uriString = sharedPreferences.getString(KEY_IMAGE_URI, null)
        return if (uriString != null) Uri.parse(uriString) else null
    }

    // Retrieve the user name
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_NAME, "Unknown")
    }

    // Retrieve the phone number
    fun getPhoneNumber(): String? {
        return sharedPreferences.getString(KEY_PHONE_NUMBER, "01000000000")
    }

    // Retrieve the location name
    fun getLocation(): String? {
        return sharedPreferences.getString(KEY_LOCATION, "loading")
    }

    // Retrieve latitude
    fun getLatitude(): Double {
        return sharedPreferences.getFloat(KEY_LATITUDE, 0.0f).toDouble()
    }

    // Retrieve longitude
    fun getLongitude(): Double {
        return sharedPreferences.getFloat(KEY_LONGITUDE, 0.0f).toDouble()
    }

    // Clear all stored user data (optional)
    fun clearUserData() {
        editor.clear().apply()
    }
}