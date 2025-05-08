package com.example.myapplication

import androidx.compose.runtime.MutableState

/**
 * This class is used to define the check data type
 * checked is a boolean that tracks whether the checkbox is checked or not
 * label is a string that holds the checkbox's text
 */

data class Check (
    val checked: MutableState<Boolean>,
    val label: String,
)