package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * This class is used to define the event table and entity for our database
 * @property eventid: Integer associated with the unique id for an event
 * @property event_name: String the title of an event
 * @property event_description: String a description about the event
 * @property event_location: String the location where the event is taking place
 * @property organizations: List<String> a list of orgs that are putting on the event
 * @property rsvp: Integer used as a 0 or 1 for whether an event requires RSVP
 * @property event_date: String the human legible date of an event
 * @property event_time: String human legible start time of an event
 * @property event_all_day: Integer used as a 0 or 1 for whether an event is all day or not
 * @property event_start_time: String the ISO time format of the start time of the event
 * @property event_end_time: String the ISO time format of the end time of the event
 * @property tags: List<String> a list of tags assigned to an event
 * @property event_private: Integer used as a 0 or 1 for whether an event is private or not
 * @property repeats: Integer used as a 0 or 1 for whether an event repeats or not
 * @property event_image: String the path to the image associated with the event
 * @property is_draft: Integer used as a 0 or 1 for whether an event is a draft or not
 * @property is_favorited: Boolean used to discern whether the current user has favorited this event
 */
@Entity(tableName = "events")
@TypeConverters(ListConverters::class)
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val eventid: Int,
    val event_all_day: Int,
    val event_date: String,
    val event_description: String,
    val event_end_time: String,
    val event_image: String,
    val event_location: String,
    val event_name: String,
    val event_private: Int,
    val event_start_time: String,
    val event_time: String,
    val is_draft: Int,
    val organizations: List<String>,
    val repeats: Int,
    val rsvp: Int,
    val tags: List<String>,
    val is_favorited: Boolean = false
)
