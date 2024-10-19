package com.example.samsara.screens.home

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.samsara.datasource.local.UserDataSharedPref
import com.google.android.gms.location.FusedLocationProviderClient

class HomeViewModelFactory(private val application: Application,private val userData: UserDataSharedPref, private val fusedLocationClient: FusedLocationProviderClient,var geocoder: Geocoder):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(application,userData,fusedLocationClient,geocoder) as T
    }
}