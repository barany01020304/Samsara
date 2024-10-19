package com.example.samsara.screens.details

import ApartmentRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.samsara.datamodel.ApartmentDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(private val apartmentId:Int, application: Application):AndroidViewModel(application) {
    private val repository = ApartmentRepository()
    private val _apartment =MutableLiveData<ApartmentDataModel>()
    val apartment:LiveData<ApartmentDataModel> get() = _apartment

    private val _imageUrls = MutableLiveData<List<String>>()
    val imageUrls: LiveData<List<String>> get() = _imageUrls

    private val _mapImage = MutableLiveData<String>()
    val mapImage: LiveData<String> get() = _mapImage

    private val _currentIndex = MutableLiveData<Int>()
    val currentIndex: LiveData<Int> get() = _currentIndex


    init {
        viewModelScope.launch {
            setApartment(apartmentId)
            _mapImage.value=repository.getApartments()[apartmentId].locationInMap?:""

            SetImageSwitcher()
            delay(500L)
        }

    }
    private suspend fun SetImageSwitcher(){
        _imageUrls.value =_apartment.value!!.photos
        _currentIndex.value = 0

    }
    fun setDataToCheckout(){

    }
    fun nextImage() {
        _currentIndex.value = (_currentIndex.value?.plus(1) ?: 0).coerceAtMost((_imageUrls.value?.size ?: 1) - 1)
    }

    fun previousImage() {
        _currentIndex.value = (_currentIndex.value?.minus(1) ?: 0).coerceAtLeast(0)
    }
    private suspend fun setApartment(id:Int){
        withContext(Dispatchers.IO){
            _apartment.postValue(repository.getApartments().find { it.id==id })
        }
    }
}