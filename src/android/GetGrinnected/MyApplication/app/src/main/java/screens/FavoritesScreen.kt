package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.AppRepository
import com.example.myapplication.EventCard
import com.example.myapplication.toEvent

/**
 * A composable function that represents the Favorites screen of our app.
 *
 * @param modifier Modifier to be applied to the screen layout.
 */

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    // Handles the scrolling state
    val scrollState = rememberScrollState()
    // Allows our screen to be scrollable
    LaunchedEffect(Unit) { scrollState.animateScrollTo(0) }

    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
    val typography = MaterialTheme.typography

    // Gets event Entities from local Repo
    val eventEntities by AppRepository.events
    // Converts eventEntities to events
    val event = eventEntities.map { it.toEvent() }
    // List of events sorted by time
    val events = event.sortedBy { it.event_time }
    // List of on the favorited events
    val favoritedEvents = events.filter { it.is_favorited }

    // This sets up the screen to be a column
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // We set up a row for the favorite events title
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Favorite Events",
                style = typography.headlineMedium,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(top = 16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // If they don't have any favorited events we display the text otherwise we display their events
        if (favoritedEvents.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "You haven't favorited any events yet.",
                    style = typography.titleLarge,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // For every event we fill an event card composable
            favoritedEvents.forEach { event ->
                EventCard(event = event, modifier = Modifier)
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}

/**
 * Preview used specifically for UI design purposes
 */
@Preview (showBackground = true)
@Composable
fun FavoritesPreview() {
    FavoritesScreen()
}