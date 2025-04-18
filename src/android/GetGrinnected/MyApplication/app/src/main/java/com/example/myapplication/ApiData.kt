package com.example.myapplication

/**
 * Data object of info sent to make a request to log in
 */
data class LoginRequest(
    val email: String
)

/**
 * Data object of info sent to make a signup request to signup / create a user
 */
data class SignupRequest(
    val account_username: String,
    val email: String,
)

/**
 * Data object that receives the response from the api (Subject to change based on api implementation)
 */
data class AuthResponse(
    val success: Boolean,
    val message: String
)

/**
 * Data object of info sent to verify an email is available
 */
data class EmailRequest(
    val email: String
)