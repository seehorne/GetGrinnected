package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                RetrofitApiClient.apiModel.getEvents()
            }

            val events = response.body() ?: emptyList()
            val eventssorted = events.sortedBy { it.event_time }
            val length = eventssorted.size
            // val eventsTimeFixed = fixTime(eventssorted,length)

            // These get our persistent state values for whether the user is logged in
            // and what theme they had their app set to.
            val isLoggedIn = DataStoreSettings.getLoggedIn(applicationContext).first()
            val isDarkMode = DataStoreSettings.getDarkMode(applicationContext).first()
            val darkTheme = mutableStateOf(isDarkMode)

            /* The dark theme value is passed down to the switch that we toggle it at
            and then the onToggleTheme is a lambda function that allows us to switch
            the state of dark theme, additionally the darktheme value traces into our
            theme file which allows us to change the whole app theme.
            */
            setContent {
                MyApplicationTheme(darkTheme = darkTheme.value, dynamicColor = false /* ensures our theme is displayed*/) {
                    AppNavigation(
                        darkTheme = darkTheme.value,
                        onToggleTheme = { darkTheme.value = it
                            // Used to pass the live change setting of theme down through the app to our UI switch.
                            lifecycleScope.launch {
                                DataStoreSettings.setDarkMode(applicationContext, it)
                            }},
                        event = eventssorted, // sorted list of events we are passing in
                        eventnum = length, // length of the list of events
                        startDestination = if (isLoggedIn) "main" else "welcome" // What screen to launch the app on
                    )
                }
            }
        }
    }
}

fun fixTime(aba: List<Event>, length: Int): List<Event>{
    var current = 0
    val done: MutableList<Event> = mutableListOf()
    repeat(length){
        done[current] = aba[current].copy(event_start_time = aba[current].event_start_time.toDate().formatTo("yyyy-MM-dd").toString())
                    current += 1
    }
    return(done)
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd HH:mm:ss",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}

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