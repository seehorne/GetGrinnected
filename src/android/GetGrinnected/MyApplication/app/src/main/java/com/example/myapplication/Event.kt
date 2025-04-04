package com.example.myapplication


/**
 *
 */
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
