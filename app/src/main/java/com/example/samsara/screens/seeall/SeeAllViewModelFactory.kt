package com.example.samsara.screens.seeall

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.samsara.datasource.local.UserDataSharedPref
import com.example.samsara.screens.rent.RentViewModel

class SeeAllViewModelFactory(val application: Application,val rentOrBuy: String, val nearOrTop: String):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeeAllViewModel(application,rentOrBuy,nearOrTop) as T
    }
}