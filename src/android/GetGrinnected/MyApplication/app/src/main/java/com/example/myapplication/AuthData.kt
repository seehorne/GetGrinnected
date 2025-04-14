package com.example.myapplication

/**
 * Data object of info sent to make a request to log in
 */
data class LoginRequest(
    val account_username: String,
    val password: String
)

/**
 * Data object of info sent to make a signup request to signup / create a user
 */
data class SignupRequest(
    val account_username: String,
    val email: String,
    val password: String
)

/**
 * Data object that receives the response from the api (Subject to change based on api implementation)
 */
data class AuthResponse(
    val success: Boolean,
    val message: String
)
