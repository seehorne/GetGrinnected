package com.example.myapplication

import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao

// This is a data object that we use to interact with our database through sql queries
@Dao
interface AppDao {

    // Event Operations

    // Gets you all events in a flow type so data is reactive and sorted by date and time
    @Query("SELECT * FROM events ORDER BY event_date ASC, event_start_time ASC")
    fun getAllEvents(): Flow<List<EventEntity>>



    // Gets events by a given id should probably switch this to a flow
    @Query("SELECT * FROM events WHERE eventid = :id")
    suspend fun getEventById(id: Int): EventEntity?

    // Upserts a single event (upsert means you update and insert information)
    @Upsert
    suspend fun UpsertEvent(event: EventEntity)

    // Upserts all events (upsert means you update and insert information)
    @Upsert
    suspend fun UpsertAll(events: List<EventEntity>)

    // Deletes an event given a specific id
    @Query("DELETE FROM events WHERE eventid = :id")
    suspend fun deleteEvent(id: Int)

    // Gets the list of favorite events as a flow so the data is reactive
    @Query("SELECT * FROM events WHERE is_favorited = 1")
    fun getFavoriteEvents(): Flow<List<EventEntity>>

    // Gets list of events where a data is a given day and is ordered by time as a flow so the data is reactive
    @Query("SELECT * FROM events WHERE event_date = :date ORDER BY event_start_time ASC")
    fun getEventsByDate(date: String): Flow<List<EventEntity>>

    // Updates whether an event is favorited or not basically flips the symbol
    @Query("UPDATE events SET is_favorited = :isFavorited WHERE eventid = :eventId")
    suspend fun updateFavoriteStatus(eventId: Int, isFavorited: Boolean)

    // Updates whether an event is favorited or not basically flips the symbol
    @Query("UPDATE events SET is_notification = :isNotification WHERE eventid = :eventId")
    suspend fun updateNotificationStatus(eventId: Int, isNotification: Boolean)

    // Account Operations
    // Upserts an account into the account table
    @Upsert
    suspend fun upsertAccount(account: AccountEntity)

    // Gets all accounts as a flow so data is reactive.
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    // Gets a given account by the id given
    @Query("SELECT * FROM accounts WHERE accountid = :id")
    suspend fun getAccountById(id: Int): AccountEntity?

    // Deletes a given account by the id given
    @Query("DELETE FROM accounts WHERE accountid = :id")
    suspend fun deleteAccount(id: Int)

}