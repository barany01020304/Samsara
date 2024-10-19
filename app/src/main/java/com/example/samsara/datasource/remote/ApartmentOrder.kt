package com.example.samsara.datasource.remote

import com.example.samsara.datamodel.ApartmentDataModel
import retrofit2.http.GET

interface ApartmentOrder {
    @GET("apartments.json")
   suspend fun getApartments():List<ApartmentDataModel>

}