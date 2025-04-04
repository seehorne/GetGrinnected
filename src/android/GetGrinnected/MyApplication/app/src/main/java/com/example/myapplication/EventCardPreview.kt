package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates the general look of an event card.
 *
 * @param event this is an Event object to fill the contents of the card.
 * @param modifier Modifier to be applied to the card layout.
 */

@Composable
fun EventCardPreview(event: Event, modifier: Modifier) {
    Card(
        modifier = modifier
            .size(width = 300.dp, height = 100.dp)
    ) {
        Text(
            text = "${event.title}, " +
                    "${event.time}, " +
                    "${event.location}, ",
            modifier = modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}