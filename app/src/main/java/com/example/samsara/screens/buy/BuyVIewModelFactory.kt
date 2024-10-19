package com.example.samsara.screens.buy

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.samsara.datasource.local.UserDataSharedPref


class BuyVIewModelFactory(private val application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BuyViewModel(application) as T
    }
}