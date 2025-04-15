package com.example.myapplication

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // Boolean to track whether a card is expanded
    val expanded = remember { mutableStateOf(false) }
    // Boolean to track whether a card is favorited
    val isFavorited = remember { mutableStateOf(event.is_favorited) }

    // Sets up composable to be a card for our info
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 120.dp)
            .padding(horizontal = 8.dp)
            .background(Color.White)
            .clickable
        {
                expanded.value = !expanded.value
            }
    ) {
        // Sets up a column on our card
        Column(
            modifier = Modifier
                .padding(12.dp)
                .animateContentSize()
                .background(Color.White)
        ) {
            // Makes a row within the column
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Makes a column within the row to display the name of the event
                Column(modifier = Modifier.weight(1f).background(Color.White)) {
                    Text(
                        text = event.event_name,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp).background(Color.White))

                    Text(text = "${event.event_date} at ${event.event_time}")

                    Spacer(modifier = Modifier.height(2.dp).background(Color.White))

                    Text(text = event.event_location)

                    Spacer(modifier = Modifier.height(2.dp).background(Color.White))

                    // If organizations is empty we won't include the output on the card
                    if (event.organizations.isNotEmpty()) {
                        Text(text = "Hosted by: ${event.organizations.joinToString()}")
                    }
                }
                // This is our favorite icon that is align with the column of info but beside it
                // as it is in a row.
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
            // This is our expanded view if the value is expanded we show the following info
            if (expanded.value) {
                if (event.event_description.isNotEmpty()) {
                    Text(text = "Description: ${event.event_description}")
                }

                Spacer(modifier = Modifier.height(8.dp).background(Color.White))

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
    EventCard(Event(eventid= 2, event_name = "Crafternoon", event_description =  "Lots of fun arts and crafts", event_location = "Downtown Theater", organizations = listOf("NAMI"), rsvp = 0, event_date ="2025-05-01", event_start_time = "6:30 PM", event_private = 0, event_end_time = "8:00 PM", event_time ="8:00 PM", tags =listOf("art, fun"), is_draft = 0,repeats = 0, event_image = "h", event_all_day = 0, is_favorited = true))
}
