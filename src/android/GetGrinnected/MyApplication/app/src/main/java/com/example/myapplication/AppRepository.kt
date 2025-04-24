package com.example.myapplication

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
//import com.example.myapplication.AppRepository.toUser

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

    // Private initialization of accounts as a mutable list
    private val _accounts = mutableStateOf<List<AccountEntity>>(emptyList())
    // Accessible list of accounts via a get
    val accounts: State<List<AccountEntity>> get() = _accounts

    // Private initialization of current account as a mutable state
    private val _currentAccount = mutableStateOf<AccountEntity?>(null)
    // Accessible account state via a get
    val currentAccount: State<AccountEntity?> get() = _currentAccount

    /**
     * function to initializes our database
     * @param context the local/current context of the app
     */
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

    // This function allows us to sync our local repo with our remote database and update favorited
    // events associated with the account we are currently logged in with
    suspend fun syncFromApi() {
        try {
            // Calls our getEvents function from the api and stores the response
            val response = RetrofitApiClient.apiModel.getEvents()
            // Assesses the response if successful
            if (response.isSuccessful) {
                // We store the set of favorite events (could be empty)
                val currentFavorites = _currentAccount.value?.favorited_events?.toSet() ?: emptySet()
                // We update our list of events with the set of favorite events ticked to favorite
                val events = response.body()?.map {
                    it.copy(is_favorited = currentFavorites.contains(it.eventid))
                }?.map { it.toEventEntity() }

                // If our events list isn't empty we upsert them to our local repo
                if (events != null) dao.UpsertAll(events)
            }
        } catch (e: Exception) {
            e.printStackTrace() // or log
        }
    }

    /**
     * Function to toggle favorite state, used when favoriting an event.
     * @param eventId id of the event to be toggled
     * @param isFavorited a boolean associated with whether the event is favorited or not
     */
    suspend fun toggleFavorite(eventId: Int, isFavorited: Boolean) {
        // Update the isFavorited state in the events table
        dao.updateFavoriteStatus(eventId, isFavorited)

        // Get our current account
        val currentAccount = _currentAccount.value
        // Check to make sure we actually have an account
        if (currentAccount != null) {
            // Modify the favorited_events list
            val updatedFavorites = if (isFavorited) {
                // If event is favorited we add the id to our accounts list of favorited events
                currentAccount.favorited_events + eventId
            } else {
                // If the event has been unfavorited we remove the id from our accounts list of
                // favorited events
                currentAccount.favorited_events - eventId
            }

            // Make new account instance with the new favorited events
            val updatedAccount = currentAccount.copy(favorited_events = updatedFavorites)

            // Upsert the account which updates the list of favorited events
            dao.upsertAccount(updatedAccount)

            // Update the account local state
            _currentAccount.value = updatedAccount
        }
    }

    /**
     * Function to toggle notification state, used when user wants a notification for an event.
     * @param eventId id of the event to be toggled
     * @param isNotifications a boolean associated with whether the event should notify
     */
    // 
    suspend fun toggleNotification(eventId: Int, isNotification: Boolean) {
        dao.updateNotificationStatus(eventId, isNotification)
    }

    /**
     * Function to set our account Id of the current active account of the user
     * @param accountId id of the account we are setting our active account to
     */
    suspend fun setCurrentAccountById(accountId: Int) {
        val account = dao.getAccountById(accountId)
        _currentAccount.value = account
    }

    /**
     * Function to upsert an account into the local repo
     * @param account AccountEntity to be upserted into the repo
     */
    suspend fun upsertAccount(account: AccountEntity) {
        dao.upsertAccount(account)

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

/**
 * Used to convert an accountEntity to a User
 */
fun AccountEntity.toUser(): User = User(
    accountid = this.accountid,
    account_name = this.account_name,
    email = this.email,
    profile_picture = this.profile_picture,
    favorited_events = this.favorited_events,
    drafted_events = this.drafted_events,
    favorited_tags = this.favorited_tags,
    account_description = this.account_description,
    account_role = this.account_role,
    is_followed = this.is_followed,
)

/**
 * Used to convert a User to an accountEntity
 */
fun User.toAccountEntity(): AccountEntity = AccountEntity(
    accountid = this.accountid,
    account_name = this.account_name,
    email = this.email,
    profile_picture = this.profile_picture,
    favorited_events = this.favorited_events,
    drafted_events = this.drafted_events,
    favorited_tags = this.favorited_tags,
    account_description = this.account_description,
    account_role = this.account_role,
    is_followed = this.is_followed,
)
