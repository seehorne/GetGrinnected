package com.GetGrinnected.myapplication

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// Most of this concept came straight from this tutorial https://www.youtube.com/watch?v=KFazs62lIkE
// Class for an event that we post on the snackbar
data class SnackBarEvent(
    val message : String
)

// Singleton object that sends our snackbars anywhere in the app
object SnackBarController {
    // Our private value for snackbar events that is a channel that we can listen in on
    private val _events = Channel<SnackBarEvent>()
    // Public events is a flow that we can receive the latest event to post a snackbar for
    val events = _events.receiveAsFlow()

    // Async function to send the most recent event we have to post to the snackbar
    suspend fun sendEvent(event: SnackBarEvent){
        _events.send(event)
    }
}