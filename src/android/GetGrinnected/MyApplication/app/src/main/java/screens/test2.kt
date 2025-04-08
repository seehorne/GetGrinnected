package screens

import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.net.ssl.HttpsURLConnection

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

//Example usage
fun main() {
    val url = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
    val result = getDataFromUrl(url)
    if (result != null) {
        println(result)
    } else {
        println("Failed to get data from URL")
    }
}