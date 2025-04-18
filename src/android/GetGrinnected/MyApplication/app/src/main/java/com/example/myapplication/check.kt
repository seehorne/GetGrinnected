package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class check (
    var checked: Boolean = false,
    val label: String,
)