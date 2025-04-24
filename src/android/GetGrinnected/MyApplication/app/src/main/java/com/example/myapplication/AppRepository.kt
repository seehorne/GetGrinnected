package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

/**
 * This is our App Repository it is a singleton object so that we can access tables seamlessly through
 * out the rest of the app.
 */

object AppRepository {
    // Initializes our path to our dao
    private lateinit var dao: AppDao

    // Private initialization of events as a mutable list
    private val _events = mutableStateOf<List<EventEntity>>(emptyList())
    // Accessible list of events via a get
    val events: State<List<EventEntity>> get() = _events

    // Initializes our database
    fun initialize(context: Context) {
        dao = AppDatabase.getDatabase(context).appDao()
        observeDatabase()
    }

    // This is as stated to observe our database
    private fun observeDatabase() {
        // Collect from DB and keep local state updated
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAllEvents().collect {
                _events.value = it
            }
        }
    }

    // This function allows us to sync our local repo with our remote database
    suspend fun syncFromApi() {
        try {
            val response = RetrofitApiClient.apiModel.getEvents()
            if (response.isSuccessful) {
                val events = response.body()?.map { it.toEventEntity() } ?: emptyList()
                dao.UpsertAll(events)
            }
        } catch (e: Exception) {
            e.printStackTrace() // or log
        }
    }

    // Function to toggle favorite state, used when favoriting an event.
    suspend fun toggleFavorite(eventId: Int, isFavorited: Boolean) {
        dao.updateFavoriteStatus(eventId, isFavorited)
    }

    // Function to toggle notification state, used when user wants a notification for an event.
    suspend fun toggleNotification(eventId: Int, isNotification: Boolean) {
        dao.updateNotificationStatus(eventId, isNotification)
    }
}

/**
 * Used to convert an event to an eventEntity
 */
fun Event.toEventEntity(): EventEntity = EventEntity(
    eventid = this.eventid,
    event_name = this.event_name?: "",
    event_description = this.event_description?: "",
    event_location = this.event_location?: "",
    organizations = this.organizations?: emptyList(),
    rsvp = this.rsvp,
    event_date = this.event_date?: "",
    event_time = this.event_time?: "",
    event_all_day = this.event_all_day,
    event_start_time = this.event_start_time,
    event_end_time = this.event_end_time,
    tags = this.tags,
    event_private = this.event_private,
    repeats = this.repeats,
    event_image = this.event_image ?: "",
    is_draft = this.is_draft,
    is_favorited = this.is_favorited,
    is_notification = this.is_notification
)

/**
 * Used to convert an eventEntity to an Event
 */
fun EventEntity.toEvent(): Event = Event(
    eventid = this.eventid,
    event_name = this.event_name,
    event_description = this.event_description,
    event_location = this.event_location,
    organizations = this.organizations,
    rsvp = this.rsvp,
    event_date = this.event_date,
    event_time = this.event_time,
    event_all_day = this.event_all_day,
    event_start_time = this.event_start_time,
    event_end_time = this.event_end_time,
    tags = this.tags,
    event_private = this.event_private,
    repeats = this.repeats,
    event_image = this.event_image,
    is_draft = this.is_draft,
    is_favorited = this.is_favorited,
    is_notification = this.is_notification
)

