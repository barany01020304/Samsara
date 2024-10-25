package com.example.samsara.screens.buy

import ApartmentRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datasource.local.ApartmentDatabase
import com.example.samsara.datasource.local.UserDataSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.*

class BuyViewModel( application: Application) : AndroidViewModel(application) {
    private val userData =UserDataSharedPref(application.applicationContext)

    private val repository = ApartmentRepository()
    private val db= ApartmentDatabase.getInstance(application.applicationContext).apartmentDao

    private val _apartmentsTop = MutableLiveData<List<ApartmentDataModel>>()
    val apartmentsTop: LiveData<List<ApartmentDataModel>> get() = _apartmentsTop
    private val _apartmentsNear = MutableLiveData<List<ApartmentDataModel>>()
    val apartmentsNear: LiveData<List<ApartmentDataModel>> get() = _apartmentsNear

    init {
        fetchApartments()
    }
    suspend fun saveItemToRoom(apartment:ApartmentDataModel) {
        withContext(Dispatchers.IO) {
            delay(100L)
            db.insert(apartment)
            db.getAllApartments()
        }
    }
    private fun fetchApartments() {
        viewModelScope.launch {
            val allApartments = repository.getApartments()
            setTopRatedRent(allApartments)
            setNearLocation(allApartments)
            delay(500L)
        }
    }

    private suspend fun setTopRatedRent(allApartments: List<ApartmentDataModel>) {
        withContext(Dispatchers.IO) {

            _apartmentsTop.postValue(allApartments.filter { it.rating >= 4.5 && it.type == "buy" }.sortedByDescending {  it.rating }.take(5))
        }
    }

    private suspend fun setNearLocation(allApartments: List<ApartmentDataModel>) {
        withContext(Dispatchers.IO) {
            _apartmentsNear.postValue(allApartments.filter { calcDistance(userData.getLatitude(),userData.getLongitude(),it.latitude,it.longitude)<=40 && it.type == "buy" }.sortedBy {calcDistance(userData.getLatitude(),userData.getLongitude(),it.latitude,it.longitude)  }.take(5))
        }
    }

    fun calcDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Radius of the Earth in km

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c // returns distance in kilometers
    }

}