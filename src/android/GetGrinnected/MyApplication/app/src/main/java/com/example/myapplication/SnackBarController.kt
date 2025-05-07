package com.example.myapplication

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


data class SnackBarEvent(
    val message : String
)

object SnackBarController {
    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackBarEvent){
        _events.send(event)
    }

}