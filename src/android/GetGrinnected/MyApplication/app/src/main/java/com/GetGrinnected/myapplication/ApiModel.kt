package com.GetGrinnected.myapplication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT


/**
 * Setups up the calls for how we talk with our api.
 */
interface ApiModel {
    // Async function for login request
    @POST("session/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Async function for signup request
    @POST("session/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    // Async function to get events
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

    // Async function to verify code for OTP
    @POST("session/verify")
    suspend fun verifyOTP(@Body request: VerifyRequest): Response<AuthResponse>

    // Async function to get user info given an access token
    @GET("user/data")
    suspend fun getUserData(@Header("Authorization") token: String): Response<User>

    // Async function to refresh auth tokens
    @POST("session/refresh")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<TokenRefreshResponse>

    // Async function to update the remote databases list of favorited events for a user
    @PUT("user/events/favorited")
    suspend fun updateFavorites(@Header("Authorization") token: String, @Body favorites: UpdateFavoritesRequest): Response<SimpleMessageResponse>

    // Async function to update the remote databases list of notified events for a user
    @PUT("user/events/notified")
    suspend fun updateNotifications(@Header("Authorization") token: String, @Body notifications: UpdateNotificationsRequest): Response<SimpleMessageResponse>

    // Async function to update the remote databases current username for a user
    @PUT("user/username")
    suspend fun updateUsername(@Header("Authorization") token: String, @Body usernameRequest: UpdateUsernameRequest): Response<SimpleMessageResponse>

    // Async function to delete the user account
    @DELETE("user")
    suspend fun deleteAccount(@Header("Authorization") token: String): Response<SimpleMessageResponse>

    // Async function to update the remote databases current username for a user
    @PUT("user/email")
    suspend fun updateEmail(@Header("Authorization") token: String, @Body emailRequest: UpdateEmailRequest): Response<AuthResponse>
}