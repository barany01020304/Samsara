package com.example.samsara.screens.searchscreen

import ApartmentRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datasource.local.ApartmentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchScreenViewModel(application: Application): AndroidViewModel(application) {
    private val repository =ApartmentRepository()
    private val db= ApartmentDatabase.getInstance(application.applicationContext).apartmentDao

    private val _allApartments=MutableLiveData<List<ApartmentDataModel>>()
    val allApartments:LiveData<List<ApartmentDataModel>> get() = _allApartments

    private val _searchResult=MutableLiveData<List<ApartmentDataModel>>()
    val searchResult:LiveData<List<ApartmentDataModel>> get() = _searchResult
    init {
        viewModelScope.launch {
            _allApartments.value=repository.getApartments()
            _searchResult.value= emptyList()
        }


    }
    suspend fun setSearchResult(city:String){
      val list =  repository.getApartments().filter { it.location==city}
        _searchResult.postValue(list)
    }
    suspend fun saveItemToRoom(apartment:ApartmentDataModel){
        withContext(Dispatchers.IO){
            db.insert(apartment)
            db.getAllApartments()
        }
    }
//    private suspend fun setSaveRecycle(allApartments: List<ApartmentDataModel> =repository.getApartments()) {
//        withContext(Dispatchers.IO) {
//
//            _searchResult.postValue(allApartments.filter { it.rating >= 4.5 && it.type == "Rent" }.sortedByDescending {  it.rating }.take(5))
//        }
//    }
}