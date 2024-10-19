package com.example.samsara.screens.saved

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.samsara.screens.rent.RentViewModel

class SavedViewModelFactory(private val application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedViewModel(application) as T
    }
}