package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
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
            var current = 0
            val gson = Gson()
            val listType = object : TypeToken<List<Event>>() {}.type
            val events: List<Event> = gson.fromJson(result, listType) ?: emptyList()
            val length = events.size
            var eventssorted = events.sortedBy { it.event_time }
            // val eventsTimeFixed = fixTime(eventssorted,length)
            val darkTheme = mutableStateOf(false)

            /* The dark theme value is passed down to the switch that we toggle it at
            and then the onToggleTheme is a lambda function that allows us to switch
            the state of dark theme, additionally the darktheme value traces into our
            theme file which allows us to change the whole app theme.
            */
            setContent {
                MyApplicationTheme(darkTheme = darkTheme.value) {
                    AppNavigation(
                        darkTheme = darkTheme.value,
                        onToggleTheme = { darkTheme.value = it },
                        event = eventssorted,
                        eventnum = length
                    )
                }
            }
        }
    }
}

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