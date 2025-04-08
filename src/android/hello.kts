import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.net.ssl.HttpsURLConnection

// Test1(first) -> String
// first - String
// Description-Takes a strings and concatonates Hello to the submitted string
fun test1(first: String): Unit {
    println("Hello" + " " + first)
}

// Test2(add) -> Int
// add - Int
// Description-Takes an integer and adds three
fun test2(add: Int): Int {
    return add + 3
}

// recursive(iterate) -> Int
// iterate - Int
// Description-recursivly iterates a value for 100 times
fun recursive(iterate: Int): Int{
    if (iterate == 100) {
        return iterate
    }
    return recursive(iterate + 1)
}

// recursive2(iterate, final) -> Int
// iterate - Int
// final - Int
// Description-recursivly iterates a value for 100 times
fun recursive2(iterate: Int, final: Int): Int{
    if (iterate == final) {
        return iterate
    }
    return recursive2((iterate + 1), final)
}

// loop() -> void
// Description-loops over a list and prints the elements
fun loop(){
val colors = listOf("Red", "Green", "Blue")
for (color in colors) {
    print("$color \n")
    }
}

// loop2(tasks) -> void
// tasks - array
// Description-loops over a list and prints the elements
fun loop2(tasks: Array<String>): Array<String>{
    for (String in tasks) {
        print("$String")
    }
    return tasks
}

// add(a, b) -> Int
// a - Int
// b - Int
// Description-adds a and b together
fun add(a: Int, b: Int): Int {
    val sum = a + b
    return sum
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




fun main(){
    val url = "https://node16049-csc324--spring2025.us.reclaim.cloud/events"
    val result = getDataFromUrl(url)
    if (result != null) {
        println(result)
    } else {
        println("Failed to get data from URL")
    }
    println("Hello World")
    test1("Ethan")
    println(test2(7))
    println(recursive(1))
    println(result)
    loop()
    val trialRun = arrayOf(" learn Kotlin ", " sleep ", " suffer ")
    loop2(trialRun)
}
main()