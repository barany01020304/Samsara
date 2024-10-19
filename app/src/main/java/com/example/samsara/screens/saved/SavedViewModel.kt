package com.example.samsara.screens.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datasource.local.ApartmentDatabase
import kotlinx.coroutines.launch

class SavedViewModel(application: Application):AndroidViewModel(application) {
    private val db =ApartmentDatabase.getInstance(application.applicationContext).apartmentDao
    private var _ApartmentList=MutableLiveData<List<ApartmentDataModel>>()
    val ApartmentList:LiveData<List<ApartmentDataModel>>get() = _ApartmentList
    init {
        viewModelScope.launch {
          _ApartmentList.postValue(setDataSaved())
        }
    }
  private suspend fun setDataSaved():List<ApartmentDataModel>{
     return db.getAllApartments()

    }
}