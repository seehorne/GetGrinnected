package com.GetGrinnected.myapplication

import android.content.Context
import kotlinx.coroutines.flow.first
import retrofit2.Response

/**
 * THIS FILE AS A WHOLE: This file is meant to be used for any general utility helper functions
 * to use to contact the API in a given way.
 */

/**
 * Used to safely attempt to make an authorized api call as it handles checking the access token is
 * valid and if not makes a refresh call for our tokens and then reattempts the api call with the
 * newly refreshed access token
 * @param context the current context of the app
 * @param apiCall the api call you are trying to make
 * @return the response of the api call
 */
suspend fun <T> safeApiCall(context: Context, apiCall: suspend (accessToken: String) -> Response<T>): Response<T> {
    // We get our accessToken from our storage
    val accessToken = DataStoreSettings.getAccessToken(context).first()
    // Try the call with our current access token
    var response = apiCall("Bearer $accessToken")

    // If we get a 403 error this means we don't have a valid access token or it has expired
    if (response.code() == 403) {
        // We get our refreshToken from our storage
        val refreshToken = DataStoreSettings.getRefreshToken(context).first()

        try {
            // Make a refresh token call
            val refreshResponse = RetrofitApiClient.apiModel.refreshToken("Bearer $refreshToken")

            // Assuming this is successful
            if (refreshResponse.isSuccessful) {
                // Set our new tokens
                val newAccessToken = refreshResponse.body()?.access_token
                val newRefreshToken = refreshResponse.body()?.refresh_token

                // Check that we have tokens we received from the api
                if (!newAccessToken.isNullOrEmpty() && !newRefreshToken.isNullOrEmpty()) {
                    // Store the new tokens we received
                    DataStoreSettings.setAccessToken(context, newAccessToken)
                    DataStoreSettings.setRefreshToken(context, newRefreshToken)

                    // Try our api call we wanted to make originally again with the new access token
                    response = apiCall("Bearer $newAccessToken")
                } // Would just return failing response if these tokens were not received this should
                // never happen
            } // This failing would imply that our check on build didn't refresh the token which again shouldn't
            // not happen not sure what error/how to handle that yet.
            // This error would be network related and thus we would just want to log the error and
            // return the response.
        } catch (e: Exception) {
            e.printStackTrace() // Log our error
        }
    }
    return response // the response of the outcome ideally of the api call we wanted to make.
}
