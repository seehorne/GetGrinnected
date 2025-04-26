package com.example.myapplication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Setups up the calls for how we talk with our api.
 */
interface ApiModel {
    // Async function for login request
    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Async function for signup request
    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    // Async function to get events
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

    // Async function to verify code for OTP
    @POST("user/verify")
    suspend fun verifyOTP(@Body request: VerifyRequest): Response<AuthResponse>

}