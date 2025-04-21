package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
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
            val events = response.body() ?: emptyList<Event>()
            val eventssorted = events.sortedBy { it.event_start_time }
            val eventsTimeFixed = fixTime(eventssorted)
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
            val tagsString = mutableListOf<String>()
            val tags = mutableListOf<Check>()
            var current2 = 0
            var tagstotal = 0
            repeat(length){
                tagstotal = 0
                repeat(events[current2].tags.size){
                    if (tagsString.contains(events[current2].tags[tagstotal]) == false) {
                        tagsString.add(events[current2].tags[tagstotal])
                    }
                    tagstotal += 1
                }
                current2 += 1
            }
            var num = 0
            repeat(tagsString.size){
                tags.add(Check(false,tagsString[num]))
                num += 1
            }
            setContent {
                MyApplicationTheme(darkTheme = darkTheme.value, dynamicColor = false /* ensures our theme is displayed*/) {
                    AppNavigation(
                        darkTheme = darkTheme.value,
                        onToggleTheme = {
                            darkTheme.value = it
                            // Used to pass the live change setting of theme down through the app to our UI switch.
                            lifecycleScope.launch {
                                DataStoreSettings.setDarkMode(applicationContext, it)
                            }
                        },
                        event = eventsTimeFixed, // sorted list of events we are passing in
                        eventnum = length, // length of the list of events
                        tags = tags,
                        modifier = Modifier,
                        startDestination = if (isLoggedIn) "main" else "welcome" // What screen to launch the app on
                    )
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
    var why = null
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
 * Ethan Hughes
 *
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
 * Ethan Hughes
 *
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