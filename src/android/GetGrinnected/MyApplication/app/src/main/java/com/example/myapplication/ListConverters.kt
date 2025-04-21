package com.example.myapplication

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Set of converters for types so that our tables can exist as we originally intended since
 * the we are using doesn't initially like lists as column values.
 */
class ListConverters {
    private val gson = Gson()

    // For List<String>
    @TypeConverter
    fun stringListToString(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun stringToStringList(value: String): List<String> =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type) ?: emptyList()

    // For List<Int>
    @TypeConverter
    fun intListToString(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun stringToIntList(value: String): List<Int> =
        gson.fromJson(value, object : TypeToken<List<Int>>() {}.type) ?: emptyList()

    // For Boolean stored as Int
    @TypeConverter
    fun intToBoolean(value: Int): Boolean = value == 1

    @TypeConverter
    fun booleanToInt(value: Boolean): Int = if (value) 1 else 0
}