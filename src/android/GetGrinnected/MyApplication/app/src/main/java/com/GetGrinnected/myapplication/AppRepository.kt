package com.GetGrinnected.myapplication

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import java.util.Date

/**
 * This is our App Repository it is a singleton object so that we can access tables seamlessly through
 * out the rest of the app.
 */

object AppRepository {
    // Initializes our path to our dao
    private lateinit var dao: AppDao
    // Initializes our context of our app
    private lateinit var appContext: Context

    // Private initialization of events as a mutable list
    private val _events = mutableStateOf<List<EventEntity>>(emptyList())
    // Accessible list of events via a get
    val events: State<List<EventEntity>> get() = _events

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
        appContext = context.applicationContext
        observeDatabase()
    }

    // This is as stated to observe our database
    private fun observeDatabase() {
        // Collect from DB and keep local state updated
        CoroutineScope(Dispatchers.IO).launch {
            dao.getAllEvents().collect {
                _events.value = it
                deleteExpiredEvents()
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
                // We store the set of notified events (could be empty)
                val currentNotified = _currentAccount.value?.notified_events?.toSet() ?: emptySet()
                // We update our list of events with the set of favorite events ticked to favorite
                // and now we adjust the time zones of dates as we were not in the write time zone with our
                // ISO info used the help functions Ethan made in the Main Activity
                val events = response.body()?.map {
                    // Sets the Start time to our local timezone
                    val localStartTime = it.event_start_time.toDate().formatTo("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    // Sets the end time to our local timezone
                    val localEndTime = it.event_end_time.toDate().formatTo("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    if (localStartTime != null) {
                        if (localEndTime != null) {
                            it.copy(
                                event_start_time = localStartTime,
                                event_end_time = localEndTime,
                                is_favorited = currentFavorites.contains(it.eventid),
                                is_notification = currentNotified.contains(it.eventid)
                            )
                        }
                    }
                }?.map { it.toEventEntity() }


                // If our events list isn't empty we upsert them to our local repo
                if (events != null) dao.UpsertAll(events)

                // Delete any events that have expired
                deleteExpiredEvents()

                // sync our current account data with the data on the api
                syncAccountData(appContext)
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
                currentAccount.favorited_events?.plus(eventId)
            } else {
                // If the event has been unfavorited we remove the id from our accounts list of
                // favorited events
                currentAccount.favorited_events?.minus(eventId)
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

        // Get our current account
        val currentAccount = _currentAccount.value
        // Check to make sure we actually have an account
        if (currentAccount != null) {
            // Modify the notified_events list
            val updatedNotifications = if (isNotification) {
                // If event is notified we add the id to our accounts list of notified events
                currentAccount.notified_events?.plus(eventId)
            } else {
                // If the event has been unnotified we remove the id from our accounts list of
                // notified events
                currentAccount.notified_events?.minus(eventId)
            }

            // Make new account instance with the new notified events
            val updatedAccount = currentAccount.copy(notified_events = updatedNotifications)

            // Upsert the account which updates the list of notified events
            dao.upsertAccount(updatedAccount)

            // Update the account local state
            _currentAccount.value = updatedAccount
        }
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

    /**
     * This function deletes events that have expired
     */
    private suspend fun deleteExpiredEvents() {
        val today = Date().formatTo("yyyy-MM-dd")
        // number of events deleted
        val numDeletedEvents = dao.deleteExpiredEvents(today)
        // If we deleted events handle cleanup otherwise not necessary
        if(numDeletedEvents > 0){
            // Handle cleanup of hanging favorites or notified events
            deleteExpiredAccountFavoritesAndNotifications()
        }
    }

    /**
     * This function handles cleanup of favorited events that have passed or old notified events
     * that have since passed.
     */
    private suspend fun deleteExpiredAccountFavoritesAndNotifications() {
        // Get current account if no current account we just return
        val currentAccount = _currentAccount.value ?: return

        // We get the list of the current events in the database
        val currentEventIds = dao.getAllEventIds()

        // Remove any favorited ids that no longer exist
        val updatedFavorites = currentAccount.favorited_events?.filter { currentEventIds.contains(it) }
        // Remove any notified ids that no longer exist
        val updatedNotifications = currentAccount.notified_events?.filter { currentEventIds.contains(it) }

        // Compare the sets  and if they differ we update them otherwise we do nothing
        if (updatedFavorites != currentAccount.favorited_events || updatedNotifications != currentAccount.notified_events) {
            val updatedAccount = currentAccount.copy(
                favorited_events = updatedFavorites,
                notified_events = updatedNotifications
            )
            // Update out account
            dao.upsertAccount(updatedAccount)
            // Set our current account to this updated version
            _currentAccount.value = updatedAccount
        }
    }

    /**
     * Syncs favorited events data and notified events data with the remote database
     * @param context is the current context of the app
     */
    suspend fun syncAccountData(context: Context) {
        // We get the current account if this value is null we return since they don't have an
        // account (ie a first time log in)
        val account = _currentAccount.value ?: return

        // We make our favorites call with a placeholder for token as we get our token inside
        // the safe API call
        safeApiCall(appContext) { token ->
            RetrofitApiClient.apiModel.updateFavorites(token, UpdateFavoritesRequest(account.favorited_events ?: emptyList()))
        }

        // We make our notifications call with a placeholder for token as we get our token inside
        // the safe API call
        safeApiCall(appContext) { token ->
            RetrofitApiClient.apiModel.updateNotifications(token, UpdateNotificationsRequest(account.notified_events ?: emptyList()))
        }
    }

    /**
     * Syncs local username with remote username.
     * @param username is the new username we will be switching our username to
     */
    suspend fun syncUsername(username: String) {
        // We make our username call with a placeholder for token as we get our token inside
        // the safe API call
        safeApiCall(appContext) { token ->
            RetrofitApiClient.apiModel.updateUsername(token, UpdateUsernameRequest(username))
        }
    }

    /**
     * Deletes user account
     * @param context the current context of the app.
     * @return a boolean value associated with whether deletion was successful or not
     */
    suspend fun deleteAccount(context: Context): Boolean {
        val response = safeApiCall(context) { token ->
            RetrofitApiClient.apiModel.deleteAccount(token)
        }
        return response.isSuccessful
    }

    /**
     * Syncs local email with remote email.
     * @param email is the new email we will be switching our email to
     */
    suspend fun syncEmail(email: String) {
        // We make our email call with a placeholder for token as we get our token inside
        // the safe API call
        safeApiCall(appContext) { token ->
            RetrofitApiClient.apiModel.updateEmail(token, UpdateEmailRequest(email))
        }
    }
}

/**
 * Used to convert an event to an eventEntity
 */
fun Event.toEventEntity(): EventEntity = EventEntity(
    eventid = this.eventid,
    event_name = this.event_name,
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
    profile_picture = this.profile_picture?: "",
    favorited_events = this.favorited_events?: emptyList(),
    drafted_events = this.drafted_events?: emptyList(),
    favorited_tags = this.favorited_tags?: emptyList(),
    account_description = this.account_description?: "",
    account_role = this.account_role,
    is_followed = this.is_followed,
    notified_events = this.notified_events?: emptyList()
)

/**
 * Used to convert a User to an accountEntity
 */
fun User.toAccountEntity(): AccountEntity = AccountEntity(
    accountid = this.accountid,
    account_name = this.account_name,
    email = this.email,
    profile_picture = this.profile_picture?: "",
    favorited_events = this.favorited_events?: emptyList(),
    drafted_events = this.drafted_events?: emptyList(),
    favorited_tags = this.favorited_tags?: emptyList(),
    account_description = this.account_description?: "",
    account_role = this.account_role,
    is_followed = this.is_followed,
    notified_events = this.notified_events?: emptyList()
)
