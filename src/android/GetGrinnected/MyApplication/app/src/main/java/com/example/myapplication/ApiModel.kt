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
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Async function for signup request
    @POST("signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    // Async function to check if an email is available
    @POST("checkemail")
    suspend fun checkemail(@Body request: EmailRequest): Response<AuthResponse>

    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

}