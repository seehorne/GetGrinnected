package com.example.myapplication

/**
 * This class is used to define the check data type
 * checked is a boolean that tracks whether the checkbox is checked or not
 * label is a string that holds the checkbox's text
 */

data class Check (
    var checked: Boolean = false,
    val label: String,
)