package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
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
            var darkTheme = false

            setContent {
                MyApplicationTheme {
                    AppNavigation(
                        darkTheme = darkTheme,
                        onToggleTheme = {darkTheme = it},
                        event = eventssorted,
                        eventnum = length
                        /* This call is how we get back into our
                        Theme.kt to change the theme of the whole app */
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