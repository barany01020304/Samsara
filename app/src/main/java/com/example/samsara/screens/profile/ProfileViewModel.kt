package com.example.samsara.screens.profile

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samsara.datasource.local.UserDataSharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.util.concurrent.Executors

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userData = UserDataSharedPref(application.applicationContext)

    private val _textData = MutableLiveData<Triple<String?, String?, String?>>()
    val textData: LiveData<Triple<String?, String?, String?>> get() = _textData

    private val _profileImage = MutableLiveData<Uri>()
    val profileImage: LiveData<Uri> get() = _profileImage

    init {
        showuser()
        _profileImage.value=userData.getImageUri()
        userData.getLocation()
        _textData.value =  Triple(userData.getUserName(), userData.getPhoneNumber(), userData.getLocation())
    }
    fun setImage(uri: Uri) {
        userData.setImageUri(uri)
        _profileImage.value =uri
    }
    private fun showuser() {
        val currentuser = Firebase.auth.currentUser
        val userData = UserDataSharedPref(getApplication<Application>().applicationContext)
        currentuser?.let {
            val name = it.displayName
            userData.setUserName(name ?: "Unknown")

            val email = it.email
            val photoUrl = it.photoUrl
            val phoneNumber: String = it.phoneNumber ?: "0100000000000"
            userData.setPhoneNumber(phoneNumber)
            userData.setPhoneNumber(name ?: "Unknown")
            //val emailVerified=it.isEmailVerified
            var image: Bitmap? = null
            val imageURL = photoUrl.toString()
            val executorService = Executors.newSingleThreadExecutor()
            executorService.execute {
                try {
                    val inm = java.net.URL(imageURL).openStream()
                    image = BitmapFactory.decodeStream(inm)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}