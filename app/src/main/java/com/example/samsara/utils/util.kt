package com.example.samsara.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun calcDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371.0 // Radius of the Earth in km

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c // returns distance in kilometers
}
fun translateText(inputText: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.ARABIC)
        .build()
    val englishArabicTranslator = Translation.getClient(options)
    val conditions = DownloadConditions.Builder().requireWifi().build()

    englishArabicTranslator.downloadModelIfNeeded(conditions)
        .addOnSuccessListener {
            englishArabicTranslator.translate(inputText)
                .addOnSuccessListener { translatedText ->
                    onSuccess(translatedText)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Translation failed")
                }
        }
        .addOnFailureListener { exception ->
            onFailure(exception.message ?: "Model download failed")
        }
}
// this fun for the upper fun
//translateText("thanks Allah it's working",
//onSuccess = { translatedText ->
//    // Handle the translated text
//    dataTextView.text=translatedText
//},
//onFailure = { errorMessage ->
//    // Handle the error
//    println("Error: $errorMessage")
//}
//)

//for dis
//fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//    val earthRadius = 6371.0 // Radius of the Earth in km
//
//    val dLat = Math.toRadians(lat2 - lat1)
//    val dLon = Math.toRadians(lon2 - lon1)
//
//    val a = sin(dLat / 2) * sin(dLat / 2) +
//            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
//            sin(dLon / 2) * sin(dLon / 2)
//
//    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//
//    return earthRadius * c // returns distance in kilometers
//}
//fun filterPlacesWithin20km(currentLat: Double, currentLon: Double, places: List<Place>): List<Place> {
//    return places.filter { place ->
//        val distance = haversine(currentLat, currentLon, place.latitude, place.longitude)
//        distance <= 20.0
//    }
//}
// Filter places within 20 km radius
//val nearbyPlaces = filterPlacesWithin20km(currentLat, currentLon, places)
//
//// Print nearby places
//nearbyPlaces.forEach { place ->
//    println("Nearby Place: ${place.name}")
//}