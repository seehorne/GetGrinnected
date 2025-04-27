package com.example.myapplication

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Creates a single instance of our datastore for our apps context. IE only 1 data store per
// context.
private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * This is our instantiation of the datastore object which allows us to keep persistent states
 * within our app. Currently we have persistent states logged in and dark or light mode.
 */
object DataStoreSettings {
    // Preference key for logged in value that we store within our datastore file
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    // Preference key for dark mode value that we store within our datastore file
    private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    // Preference key for Logged in account id to track the current logged in account
    private val LOGGED_IN_ACCOUNT_ID = intPreferencesKey("logged_in_account_id")
    // Preference key for Access token
    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    // Preference key for Refresh token
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    // Preference key for the last time we synced with the api
    private val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")

    /**
     * Setter for logged in value
     * @param context is the current context our app is running
     * @param isLoggedIn is the boolean we are setting our isloggedin preference to
     */
    suspend fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = isLoggedIn }
    }

    /**
     * Getter for logged in value
     * @param context is the current context our app is running
     * @return Flow<Boolean> this basically is a reactive boolean that allows us to update our
     * app in real time without needing to pass the value you down through functions, this boolean
     * represents our isLoggedIn value.
     */
    fun getLoggedIn(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    }

    /**
     * Setter for darkmode value
     * @param context is the current context our app is running
     * @param isDark is the boolean we are setting our isDarkMode preference to
     */
    suspend fun setDarkMode(context: Context, isDark: Boolean) {
        context.dataStore.edit { it[IS_DARK_MODE] = isDark }
    }

    /**
     * Getter for isDarkMode value
     * @param context is the current context our app is running
     * @return Flow<Boolean> this basically is a reactive boolean that allows us to update our
     * app in real time without needing to pass the value you down through functions, this boolean
     * represents our isDarkMode value.
     */
    fun getDarkMode(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { it[IS_DARK_MODE] ?: false }
    }

    /**
     * Setter for accountId value
     * @param context is the current context our app is running
     * @param accountId is the boolean we are setting our isDarkMode preference to
     */
    suspend fun setLoggedInAccountId(context: Context, accountId: Int) {
        context.dataStore.edit { it[LOGGED_IN_ACCOUNT_ID] = accountId }
    }

    /**
     * Getter for AccountID value
     * @param context is the current context our app is running
     * @return Flow<Int> this basically is a reactive value of the event id that allows us to
     * update our app in real time without needing to pass the value down through functions
     * it is simply accessible anywhere in our app.
     */
    fun getLoggedInAccountId(context: Context): Flow<Int?> {
        return context.dataStore.data.map { it[LOGGED_IN_ACCOUNT_ID] }
    }

    /**
     * Setter for Access token value
     * @param context is the current context our app is running
     * @param token is the string representation of the token
     */
    suspend fun setAccessToken(context: Context, token: String) {
        context.dataStore.edit { it[ACCESS_TOKEN] = token }
    }

    /**
     * Getter for Access Token value
     * @param context is the current context our app is running
     * @return Flow<String> this basically is a reactive value of the Access token
     */
    fun getAccessToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { it[ACCESS_TOKEN] }
    }

    /**
     * Setter for Refresh token value
     * @param context is the current context our app is running
     * @param token is the string representation of the token
     */
    suspend fun setRefreshToken(context: Context, token: String) {
        context.dataStore.edit { it[REFRESH_TOKEN] = token }
    }

    /**
     * Getter for Refresh Token value
     * @param context is the current context our app is running
     * @return Flow<String> this basically is a reactive value of the Refresh token
     */
    fun getRefreshToken(context: Context): Flow<String?> {
        return context.dataStore.data.map { it[REFRESH_TOKEN] }
    }

    /**
     * Setter for Refresh token value
     * @param context is the current context our app is running
     * @param time is a Long that is the time we are setting our last sync to
     */
    suspend fun setLastSyncTime(context: Context, time: Long) {
        context.dataStore.edit { it[LAST_SYNC_TIME] = time }
    }

    /**
     * Getter for Refresh Token value
     * @param context is the current context our app is running
     * @return Flow<Long> that is a reactive version of our last time we synced with the api
     */
    fun getLastSyncTime(context: Context): Flow<Long?> {
        return context.dataStore.data.map { it[LAST_SYNC_TIME] }
    }

    /**
     * Clears all stored preferences
     * @param context current context our  app is running
     */
    suspend fun clearUserSession(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}