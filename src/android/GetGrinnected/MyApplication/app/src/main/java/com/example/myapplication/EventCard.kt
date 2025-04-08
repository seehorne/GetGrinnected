package com.example.myapplication

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates the general look of an event card.
 * It is expandable and collapsable via a clicking interaction.
 *
 * @param event this is an Event object to fill the contents of the card.
 * @param modifier Modifier to be applied to the card layout.
 */

@Composable
fun EventCard(event: Event, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val isFavorited = remember { mutableStateOf(event.is_favorited) }

    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 120.dp)
            .padding(horizontal = 8.dp)
            .clickable {
                expanded.value = !expanded.value
            }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.event_name,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "${event.event_date} at ${event.event_time}")

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(text = event.event_location)

                    Spacer(modifier = Modifier.height(2.dp))

                    if (event.organizations.isNotEmpty()) {
                        Text(text = "Hosted by: ${event.organizations.joinToString()}")
                    }
                }
                Icon(
                    imageVector = if (isFavorited.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            isFavorited.value = !isFavorited.value
                        },
                )
            }
            if (expanded.value) {
                if (event.event_description.isNotEmpty()) {
                    Text(text = "Description: ${event.event_description}")
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (event.tags.isNotEmpty()) {
                    Text(text = "Tags: ${event.tags.joinToString()}")
                }
            }
        }
    }
}

/**
 * Preview of event card for UI adjustment purposes only.
 */

@Preview (showBackground = true)
@Composable
fun eventCardPreview(){
    EventCard(Event(2, "Crafternoon", "Lots of fun arts and crafts", "Downtown Theater",listOf("NAMI"), 0,"2025-05-01", "6:30 PM", 0,"8:00 PM", "8:00 PM", listOf("art, fun"), 0,0,"h", 0, true))
}
