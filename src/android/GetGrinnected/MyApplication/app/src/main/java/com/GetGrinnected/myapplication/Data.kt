package com.GetGrinnected.myapplication

data class Data(
    val AllDay: Boolean,
    val Audience: List<String>,
    val Date: String,
    val Description: String,
    val EndTimeISO: String,
    val ID: Int,
    val Location: String,
    val Org: String,
    val StartTimeISO: String,
    val Tags: List<String>,
    val Time: String,
    val Title: String
)