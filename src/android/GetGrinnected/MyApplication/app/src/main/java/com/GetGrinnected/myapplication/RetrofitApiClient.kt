package com.GetGrinnected.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * With retrofit you have to make a client instance and so this is ours for our API
 */
object RetrofitApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://node16049-csc324--spring2025.us.reclaim.cloud/") // Our default Url
        .addConverterFactory(GsonConverterFactory.create()) // Used to convert from JSON to our data structures
        .build() // Building this retrofit instance

    // This is our instance of our Retrofit model to use for our api calls
    val apiModel: ApiModel = retrofit.create(ApiModel::class.java)
}