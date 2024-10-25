package com.example.samsara.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.samsara.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    val a =
        sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
            dLon / 2
        ) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c // returns distance in kilometers
}

fun translateText(inputText: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
    val options = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.ARABIC).build()
    val englishArabicTranslator = Translation.getClient(options)
    val conditions = DownloadConditions.Builder().requireWifi().build()

    englishArabicTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener {
            englishArabicTranslator.translate(inputText).addOnSuccessListener { translatedText ->
                    onSuccess(translatedText)
                }.addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Translation failed")
                }
        }.addOnFailureListener { exception ->
            onFailure(exception.message ?: "Model download failed")
        }
}

fun Fragment.firebaseAuthWithGoogle(idToken: String, mAuth: FirebaseAuth) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
//            Toast.makeText(context, "signInWithCredential:success", Toast.LENGTH_LONG)
//                .show() //updateUI(currentuser)
        } else {                // If sign in fails, display a message to the user.
            Toast.makeText(
                context, "signInWithCredential:failure" + task.exception, Toast.LENGTH_LONG
            ).show()                //updateUI(null)


        }
    }
}

fun Fragment.googleSignInClient(): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
        getString(
            R.string.default_web_client_id
        )
    ).requestEmail().build()
    return GoogleSignIn.getClient(requireActivity(), gso)
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