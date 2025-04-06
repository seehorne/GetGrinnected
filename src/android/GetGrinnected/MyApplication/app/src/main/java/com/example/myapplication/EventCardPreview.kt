package com.example.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates the general look of an event card.
 *
 * @param event this is an Event object to fill the contents of the card.
 * @param modifier Modifier to be applied to the card layout.
 */

@Composable
fun EventCardPreview(event: Event, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .size(width = 320.dp, height = 120.dp)
            .padding(horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = event.title,
                fontWeight = FontWeight.Bold
            )
            Text(text = "${event.date} at ${event.time}")
            Text(text = event.location)
            if (event.organizations.isNotEmpty()) {
                Text(text = "Hosted by: ${event.organizations.joinToString()}")
            }

        }
    }
}