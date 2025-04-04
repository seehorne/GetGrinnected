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
 * Creates 
 */

@Composable
fun EventCard(event: Event, modifier: Modifier) {
    Card(
        modifier = modifier
            .size(width = 300.dp, height = 100.dp)
    ) {
        Text(
            text = "Event Location, " +
                    "Event Time, " +
                    "Event Description",
            modifier = modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}