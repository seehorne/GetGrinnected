package com.example.myapplication

import android.os.Build
import screens.LoginScreen
import screens.SignupScreen
import screens.WelcomeScreen
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
            var darkTheme by remember { mutableStateOf(false) }
            setContent {
                MyApplicationTheme {
                    AppNavigation(
                        darkTheme = darkTheme,
                        onToggleTheme = {darkTheme = it},
                        event = events
                        /* This call is how we get back into our
                        Theme.kt to change the theme of the whole app */
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