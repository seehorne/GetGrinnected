package com.example.myapplication.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    private const val baseUrl = "https://api.weatherapi.com/v1/current.json?key=235ac1fa18c943eba0632820250804&q=London&aqi=no

    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val eventApi : EventApi = getInstance().create(EventApi::class.java)
}