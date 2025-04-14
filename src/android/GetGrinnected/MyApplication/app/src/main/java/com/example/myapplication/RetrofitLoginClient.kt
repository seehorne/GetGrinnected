package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * With retrofit you have to make a client instance and so this is ours for the login process
 */
object RetrofitLoginClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://node16049-csc324--spring2025.us.reclaim.cloud/")
        .addConverterFactory(GsonConverterFactory.create()) // Used to convert from JSON to our data structures
        .build()

    val authModel: AuthModel = retrofit.create(AuthModel::class.java)
}