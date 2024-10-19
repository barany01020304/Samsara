package com.example.samsara.datasource.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitConfig {
    private const val BASE_URL="https://samsara-d2a76-default-rtdb.firebaseio.com/"
    private val retrofit :Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
    fun retrofitCreate():ApartmentOrder= retrofit.create(ApartmentOrder::class.java)
}