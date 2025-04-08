package com.example.myapplication.api

data class DAtaItem(
    val event_all_day: String,
    val event_date: String,
    val event_description: String,
    val event_end_time: String,
    val event_image: Any,
    val event_location: String,
    val event_name: String,
    val event_private: String,
    val event_start_time: String,
    val event_time: String,
    val eventid: String,
    val is_draft: String,
    val organizations: List<String>,
    val repeats: String,
    val rsvp: String,
    val tags: List<String>
)