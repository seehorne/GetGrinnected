package com.example.myapplication.ui.theme

data class testingItem(
    val event_all_day: Int,
    val event_date: String,
    val event_description: String,
    val event_end_time: String,
    val event_image: Any,
    val event_location: String,
    val event_name: String,
    val event_private: Int,
    val event_start_time: String,
    val event_time: String,
    val eventid: Int,
    val is_draft: Int,
    val organizations: List<String>,
    val repeats: Int,
    val rsvp: Int,
    val tags: List<String>
)