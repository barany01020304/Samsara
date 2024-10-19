package com.example.samsara.screens.rent

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.samsara.datasource.local.UserDataSharedPref


class RentVIewModelFactory(private val application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RentViewModel(application) as T
    }
}