package com.GetGrinnected.myapplication.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.GetGrinnected.myapplication.AppRepository
import com.GetGrinnected.myapplication.Event
import com.GetGrinnected.myapplication.toEvent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn


class SearchViewModel : ViewModel() {
    // Gets events from our repo
    private val eventEntities by AppRepository.events
    // Converts them to event data type
    val events = eventEntities.map { it.toEvent() }
    private val eventFlow = flowOf(events)
    var searchQuery by mutableStateOf("")
    val searchResults: StateFlow<List<Event>> =
    snapshotFlow { searchQuery }
    .combine(eventFlow)
    { searchQuery, event ->
        when {
            searchQuery.isNotEmpty() -> event.filter {
                events[0].event_description.contains(searchQuery, ignoreCase = true)
            }
            else -> events
        }
    }.stateIn(
    scope = viewModelScope,
    initialValue = emptyList(),
    started = SharingStarted.WhileSubscribed(5_000)
    )
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }
}

//fun contains(event: List<Event>): Boolean{
//    for (e in event.indices){
 //       if
 //   }
 //   return month
//}



