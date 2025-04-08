package com.example.myapplication


/**
 * This class is used to define the event data type
 */
data class Event(
    val eventid: Int,
    val event_name: String,
    val event_description: String,
    val event_location: String,
    val organizations: List<String>,
    val rsvp: Int,
    val event_date: String,
    val event_time: String,
    val event_all_day: Int,
    val event_start_time: String,
    val event_end_time: String,
    val tags: List<String>,
    val event_private: Int,
    val repeats: Int,
    val event_image: String?,
    val is_draft: Int,
    val is_favorited: Boolean = false
)

