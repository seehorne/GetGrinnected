package com.example.myapplication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * Setups up the calls for how we talk with our api.
 */
interface AuthModel {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("signup")
    suspend fun  signup(@Body request: SignupRequest): Response<AuthResponse>
}