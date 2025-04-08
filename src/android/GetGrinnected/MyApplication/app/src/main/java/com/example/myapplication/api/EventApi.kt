package com.example.myapplication.api

import androidx.room.Query
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApi {
    @GET("v1/current")
    suspend fun getEvent(
        @Query("key") apikey : String,
        @Query("q") title : String
    )
}