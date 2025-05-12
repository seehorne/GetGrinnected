package com.GetGrinnected.myapplication

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
    val username: String,
    val email: String)

/**
 * Data object that receives the response from the api (Subject to change based on api implementation)
 */
data class AuthResponse(
    val message: String,
    val refresh_token: String?,
    val access_token: String?
)

/**
 * Data object of info sent to verify a code
 */
data class VerifyRequest(
    val email: String,
    val code: String
)

/**
 * Data object of info sent to refresh Tokens
 */
data class TokenRefreshResponse(
    val message: String,
    val refresh_token: String,
    val access_token: String
)

/**
 * Data object of info sent to update favorited events for a user
 */
data class UpdateFavoritesRequest(
    val favorited_events: List<Int>
)

/**
 * Data object of info sent to update notified events for a user
 */
data class UpdateNotificationsRequest(
    val notified_events: List<Int>
)

/**
 * Trivial response for message to check a success
 */
data class SimpleMessageResponse(
    val message: String
)

/**
 * Data object of info sent to update a username for a user
 */
data class UpdateUsernameRequest(
    val username: String
)

/**
 * Data object of info sent to update an email for a user
 */
data class UpdateEmailRequest(
    val new_email: String
)