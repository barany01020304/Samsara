package com.example.samsara.screens.rent

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
import com.example.samsara.utils.calcDistance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RentViewModel( application: Application) : AndroidViewModel(application) {
    private val userData =UserDataSharedPref(application.applicationContext)
    private val repository = ApartmentRepository()
    private val db=ApartmentDatabase.getInstance(application.applicationContext).apartmentDao

    private val _apartmentsTop = MutableLiveData<List<ApartmentDataModel>>()
    val apartmentsTop: LiveData<List<ApartmentDataModel>> get() = _apartmentsTop

    private val _apartmentsNear = MutableLiveData<List<ApartmentDataModel>>()
    val apartmentsNear: LiveData<List<ApartmentDataModel>> get() = _apartmentsNear

    init {
        fetchApartments()
    }
 suspend fun saveItemToRoom(apartment:ApartmentDataModel){
     withContext(Dispatchers.IO){
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
            val list=allApartments.filter { it.rating >= 4.5 && it.type == "Rent" }.sortedByDescending {  it.rating }

            _apartmentsTop.postValue(list.take(5))
        }
    }

    private suspend fun setNearLocation(allApartments: List<ApartmentDataModel>) {
        withContext(Dispatchers.IO) {
            val list=allApartments.filter { calcDistance(userData.getLatitude(),userData.getLongitude(),it.latitude,it.longitude)<=40 && it.type == "Rent" }.sortedBy {  calcDistance(userData.getLatitude(),userData.getLongitude(),it.latitude,it.longitude) }
            _apartmentsNear.postValue(list.take(5))
        }
    }



}