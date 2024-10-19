package com.example.samsara.screens.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samsara.datasource.local.UserDataSharedPref

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userData = UserDataSharedPref(application.applicationContext)

    private val _textData = MutableLiveData<Triple<String?, String?, String?>>()
    val textData: LiveData<Triple<String?, String?, String?>> get() = _textData

    private val _profileImage = MutableLiveData<Uri>()
    val profileImage: LiveData<Uri> get() = _profileImage

    init {
        _profileImage.value=userData.getImageUri()
        userData.getLocation()
        _textData.value =  Triple(userData.getUserName(), userData.getPhoneNumber(), userData.getLocation())
    }
    fun setImage(uri: Uri) {
        userData.setImageUri(uri)
        _profileImage.value =uri
    }
}