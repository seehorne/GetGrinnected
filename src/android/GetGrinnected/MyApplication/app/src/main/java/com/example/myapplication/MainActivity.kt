package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection



/**
 * Our main used to run and create our app. Currently utilizes the AppNavigator function at
 * creation.
 */
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val url = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
            val result = getDataFromUrl(url)
            val events = Gson().fromJson(result, Event::class.java)
            MyApplicationTheme {
                    AppNavigation(
                        event = events
                    )
            }
        }
    }


}

data class Event(
    val title: String,
    val description: String,
    val organizations: List<String>,
    val date: String, //Not sure what to make these actually so this is temp
    val time: String, //Not sure what to make these actually so this is temp
    val location: String, //Not sure what to make this rn
    val isFavorited: Boolean,
    val tags: List<String>,
    val isDraft: Boolean,

    )

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