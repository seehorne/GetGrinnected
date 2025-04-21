package com.example.myapplication

import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao

@Dao
interface AppDao {

    // Event Operations
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE eventid = :id")
    suspend fun getEventById(id: Int): EventEntity?

    @Upsert
    suspend fun UpsertEvent(event: EventEntity)

    @Upsert
    suspend fun UpsertAll(events: List<EventEntity>)

    @Query("DELETE FROM events WHERE eventid = :id")
    suspend fun deleteEvent(id: Int)

    @Query("SELECT * FROM events WHERE is_favorited = 1")
    fun getFavoriteEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE event_date = :date ORDER BY event_start_time ASC")
    fun getEventsByDate(date: String): Flow<List<EventEntity>>

    @Query("UPDATE events SET is_favorited = :isFavorited WHERE eventid = :eventId")
    suspend fun updateFavoriteStatus(eventId: Int, isFavorited: Boolean)



    // Account Operations
    @Upsert
    suspend fun insertAccount(account: AccountEntity)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE accountid = :id")
    suspend fun getAccountById(id: Int): AccountEntity?

    @Query("DELETE FROM accounts WHERE accountid = :id")
    suspend fun deleteAccount(id: Int)

    /* Combined Operations */
    @Query("UPDATE accounts SET favorited_events = :eventIds WHERE accountid = :accountId")
    suspend fun updateFavoriteEvents(accountId: Int, eventIds: List<Int>)

    @Query("SELECT * FROM events WHERE eventid IN (:eventIds)")
    suspend fun getEventsByIds(eventIds: List<Int>): List<EventEntity>
}