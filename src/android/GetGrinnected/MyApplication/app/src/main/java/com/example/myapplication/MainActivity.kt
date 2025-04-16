package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.net.ssl.HttpsURLConnection



/**
 * Our main used to run and create our app. Currently utilizes the AppNavigator function at
 * creation.
 */
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val url = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
            val result = withContext(Dispatchers.IO) {
                getDataFromUrl(url)
            }
            val gson = Gson()
            val listType = object : TypeToken<List<Event>>() {}.type
            val events: List<Event> = gson.fromJson(result, listType) ?: emptyList()
            val length = events.size
            // sorts events by time
            val eventssorted = events.sortedBy { it.event_start_time }
            val eventsTimeFixed = fixTime(eventssorted)
            var darkTheme = false
            val tags = mutableListOf<String>()
            var current2 = 0
            var tagstotal = 0
            repeat(length){
                tagstotal = 0
                repeat(events[current2].tags.size){
                    if (tags.contains(events[current2].tags[tagstotal]) == false) {
                        tags.add(events[current2].tags[tagstotal])
                    }
                    tagstotal += 1
                }
                current2 += 1
            }
            setContent {
                MyApplicationTheme {
                    AppNavigation(
                        darkTheme = darkTheme,
                        onToggleTheme = {darkTheme = it},
                        event = eventsTimeFixed,
                        eventnum = length,
                        tags = tags
                        /* This call is how we get back into our
                        Theme.kt to change the theme of the whole app */
                    )
                }
            }
        }
    }
}

/**
 * Ethan Hughes
 *
 * a function that grabs a json from a url
 *
 * @param url a string that is a valid url link
 */
fun getDataFromUrl(url: String): String? {
    var connection: HttpsURLConnection? = null
    return try {
        val urlObj = URL(url)
        connection = urlObj.openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            response.toString()
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        connection?.disconnect()
    }
}

/**
 * Ethan Hughes
 *
 * a function that changes the timezone of elements in json list of event objects to the timezone of the device
 *
 * @param aba events list
 * @param length the number of elements in aba
 */
fun fixTime(aba: List<Event>): List<Event>{
    var current = 0
    val done = mutableListOf<Event>()
    var why = null
    repeat(aba.size){
        done.add(aba[current].copy(
            // Takes care of null cases so that java can be turned into kotlin
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