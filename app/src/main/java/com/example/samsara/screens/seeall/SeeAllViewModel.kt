package com.example.samsara.screens.seeall

import ApartmentRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService
import com.example.samsara.datasource.local.ApartmentDatabase
import com.example.samsara.datasource.local.UserDataSharedPref
import com.example.samsara.utils.calcDistance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeeAllViewModel(application: Application, val rentOrBuy: String, val nearOrTop: String) : AndroidViewModel(application) {
    private val userData =UserDataSharedPref(application.applicationContext)
    private val repository = ApartmentRepository()
    private val db= ApartmentDatabase.getInstance(application.applicationContext).apartmentDao
    private val _listOfApartment = MutableLiveData<List<ApartmentDataModel>>()
    val listOfApartment: LiveData<List<ApartmentDataModel>> get() = _listOfApartment
init {
    setListOfApartment()
}
    suspend fun saveItemToRoom(apartment:ApartmentDataModel) {
        withContext(Dispatchers.IO) {
            db.insert(apartment)
        }
    }
    private fun setListOfApartment() {
        viewModelScope.launch {
            val allApartments =repository.getApartments()
        _listOfApartment.value =chooseList(allApartments)
            delay(500L)
        }
    }

    private suspend fun chooseList(allApartments: List<ApartmentDataModel>): List<ApartmentDataModel> {
        return withContext(Dispatchers.IO) {
            when {
                rentOrBuy == "rent" && nearOrTop == "near" -> {
                    allApartments.filter { calcDistance(userData.getLatitude(), userData.getLongitude(), it.latitude, it.longitude)<=40 && it.type == "Rent" }.sortedByDescending { it.rating }
                }
                rentOrBuy == "rent" && nearOrTop == "top" -> {
                    allApartments.filter { it.rating >= 4.0 && it.type == "Rent" }
                }
                rentOrBuy == "buy" && nearOrTop == "near" -> {
                    allApartments.filter { calcDistance(userData.getLatitude(), userData.getLongitude(), it.latitude, it.longitude) <= 40 && it.type == "buy" }
                }
                rentOrBuy == "buy" && nearOrTop == "top" -> {
                    allApartments.filter { it.rating >= 4.0 && it.type == "buy" }.sortedByDescending { it.rating }
                }
                else -> {
                    emptyList()
                }
            }
        }
    }
//    private fun chooseList(allApartments: List<ApartmentDataModel>): List<ApartmentDataModel> {
//        return when {
//                rentOrBuy == "rent" && nearOrTop == "near" -> {
//                    allApartments.filter { it.rating >= 4.5 && it.type == "Rent" }.sortedByDescending { it.rating }
//                }
//                rentOrBuy == "rent" && nearOrTop == "top" -> {
//                    allApartments.filter { calcDistance(userData.getLatitude(), userData.getLongitude(), it.latitude, it.longitude) <= 40 && it.type == "Rent" }
//                }
//                rentOrBuy == "buy" && nearOrTop == "near" -> {
//                    allApartments.filter { calcDistance(userData.getLatitude(), userData.getLongitude(), it.latitude, it.longitude) <= 40 && it.type == "Buy" }
//                }
//                rentOrBuy == "buy" && nearOrTop == "top" -> {
//                    allApartments.filter { it.rating >= 4.5 && it.type == "Buy" }.sortedByDescending { it.rating }
//                }
//                else -> {
//                   return emptyList()
//                }
//
//        }
//    }
}
