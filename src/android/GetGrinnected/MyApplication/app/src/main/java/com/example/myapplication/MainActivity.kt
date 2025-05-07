package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Our main used to run and create our app. Currently utilizes the AppNavigator function at
 * creation.
 */
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This initializes our local Repo
        AppRepository.initialize(applicationContext)

        // This syncs the info we have from the API
        lifecycleScope.launch {
            // Gets the last time we synced with the API
            val lastSyncTime = DataStoreSettings.getLastSyncTime(applicationContext).first()
            // Gets the current time
            val now = System.currentTimeMillis()
            val hours = 3 // number of hours we want to wait till we refresh cache

            // If the last time we Synced is null ie we have never synced or it has been
            // More than 3 hrs (the math calculates the hours into milliseconds)
            // since our last sync
            if (lastSyncTime == null || now - lastSyncTime > hours * 60 * 60 * 1000) {
                // Syncs from API
                AppRepository.syncFromApi()
                // Sets the new LastSyncTime to now
                DataStoreSettings.setLastSyncTime(applicationContext, now)
            }
        }

        lifecycleScope.launch {
            // These get our persistent state values for whether the user is logged in
            // and what theme they had their app set to.
            val isLoggedIn = DataStoreSettings.getLoggedIn(applicationContext).first()
            val isDarkMode = DataStoreSettings.getDarkMode(applicationContext).first()
            val darkTheme = mutableStateOf(isDarkMode)
            // This is the font size we have stored for the user
            val storedFontSize = DataStoreSettings.getFontSize(applicationContext).first()

            /* The dark theme value is passed down to the switch that we toggle it at
            and then the onToggleTheme is a lambda function that allows us to switch
            the state of dark theme, additionally the darktheme value traces into our
            theme file which allows us to change the whole app theme.
            */

            // Gets the account id that we have stored
            val accountId = DataStoreSettings.getLoggedInAccountId(applicationContext).first()
            if (accountId != null) {
                // Sets our current active account in the repo to the stored account value
                AppRepository.setCurrentAccountById(accountId)
            }

            // We get our refresh token or a null value
            val refreshToken = DataStoreSettings.getRefreshToken(applicationContext).firstOrNull()

            // We check that the refresh token is none null
            if (!refreshToken.isNullOrEmpty()) {
                try {
                    // Make a refreshToken call to our API
                    val refreshResponse = RetrofitApiClient.apiModel.refreshToken("Bearer $refreshToken")

                    // If it is successful a 403 error would mean we don't have a refreshToken
                    if (refreshResponse.isSuccessful) {
                        // Get new tokens
                        val newAccessToken = refreshResponse.body()?.access_token
                        val newRefreshToken = refreshResponse.body()?.refresh_token

                        // So long as they are non-null we set the tokens
                        if (newAccessToken != null && newRefreshToken != null) {
                            // Save new tokens
                            DataStoreSettings.setAccessToken(applicationContext, newAccessToken)
                            DataStoreSettings.setRefreshToken(applicationContext, newRefreshToken)
                            // Tokens refreshed successfully! User stays logged in.
                        }
                    } else {
                        // Refresh token invalid or expired
                        DataStoreSettings.clearUserSession(applicationContext)
                    }
                } catch (e: Exception) {
                    // Network error of some sort likely unable to leave the app (this is allowed
                    // since we have the cached events so long as they have been updated within 3hrs so we
                    // won't clear session
                    Toast.makeText(applicationContext, "No internet connection. You are offline.", Toast.LENGTH_LONG).show()
                }
            }

            setContent {
                // Gets events from Repo
                val eventEntities = AppRepository.events.value
                // Turns events into event data class and then sorts them by event time
                val events = eventEntities.map { it.toEvent() }

                // We get a string set of all the distinct tags
                val tagsString = events.flatMap { it.tags }.distinct()

                // We turn this string of tags into a mutable list of check objects
                val tags = rememberSaveable(tagsString) { tagsString.map { Check(mutableStateOf(false), it) }.toMutableList() }

                // State stored as a mutable to handle the fontsize if it is empty we set it to default of medium
                val storedFontSizeState = rememberSaveable { mutableStateOf(storedFontSize ?: FontSizePrefs.DEFAULT.key) }
                // This is the actually stored state this is used as the state we pass as the "key" which
                // is effectively the string of our current selected font size
                val fontSizePref = remember(storedFontSizeState.value) {
                    FontSizePrefs.getFontPrefFromKey(storedFontSizeState.value)
                }

                MyApplicationTheme(darkTheme = darkTheme.value, dynamicColor = false /* ensures our theme is displayed*/, fontSizePrefs = fontSizePref) {
                    AppNavigation(
                        darkTheme = darkTheme.value,
                        onToggleTheme = {
                            darkTheme.value = it
                            // Used to pass the live change setting of theme down through the app to our UI switch.
                            lifecycleScope.launch {
                                DataStoreSettings.setDarkMode(applicationContext, it)
                            }
                        },
                        tags = tags.sortedBy { it.label.uppercase() },
                        startDestination = if (isLoggedIn) "main" else "welcome",
                        fontSizeSetting = fontSizePref.key,
                        onFontSizeChange = { key ->
                            // On change we set the stored value reactive state to this new value
                            storedFontSizeState.value = key
                            // Updated our preference for the set fontSize
                            lifecycleScope.launch {
                                DataStoreSettings.setFontSize(applicationContext, key)
                            }
                        }
                    ) // What screen to launch the app on
                }
            }
            // Background task launched after we set our content
            launch {
                // Number of minutes we want to delay to update the remote database
                val minutes = 1L // Currently it is every minute this can change
                // Runs indefinitely as a background task
                while (true) {
                    // Sets the delay to send in minutes
                    delay(minutes * 60 * 1000)
                    // Syncs our current local data with the remote
                    AppRepository.syncAccountData(context = applicationContext)
                }
            }
        }
    }
}
/**
 * a function that changes the timezone of elements in json list of event objects to the timezone of the device
 *
 * @param aba events list
 */
fun fixTime(aba: List<Event>): List<Event>{
    var current = 0
    val done = mutableListOf<Event>()
    // creates a variable to represent null as it cant be directly called
    val why = null
    repeat(aba.size){
        done.add(aba[current].copy(
            // Takes care of null case so that java can be turned into kotlin
            event_image = if(aba[current].event_image == why){
                "0"
            }
            else {
                aba[current].event_image
            },
            event_location = if(aba[current].event_location == why){
                "0"
            }
            else {
                aba[current].event_location
            },
            event_time = if(aba[current].event_time == why){
                "0"
            }
            else {
                aba[current].event_time
            },
            event_end_time = if(aba[current].event_end_time == why){
                "0"
            }
            else {
                aba[current].event_end_time
            },
            event_start_time = aba[current].event_start_time.toDate().formatTo("yyyy-MM-dd").toString())
        )
        current += 1
    }
    return(done)
}

/**
 * a function that turns a string into a date
 *
 * @param dateFormat string in date format
 * @param timeZone grabs the current timezone from your phone
 */
fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

/**
 * a function that turns a string into a date
 *
 * @param dateFormat string in date format
 * @param timeZone grabs the current timezone from your phone
 */
fun Date.formatTo(
    dateFormat: String,
    timeZone: TimeZone = TimeZone.getDefault(),
): String {
    val formatter = SimpleDateFormat(
        dateFormat, Locale.getDefault()
    )
    formatter.timeZone = timeZone
    return formatter.format(this)
}