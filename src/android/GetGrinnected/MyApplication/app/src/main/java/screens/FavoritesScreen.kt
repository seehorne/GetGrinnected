package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Event
import com.example.myapplication.EventCardPreview

/**
 * A composable function that represents the Favorites screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 * @param events List of events to be displayed on favorites screen.
 */

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier, events: List<Event>) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { scrollState.animateScrollTo(0) }

    val favoritedEvents = events.filter { it.isFavorited }

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Favorites", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 16.dp))
        }

        if (favoritedEvents.isEmpty()) {
            Text("You haven't favorited any events yet.", modifier = Modifier.padding(16.dp))
        } else {
            favoritedEvents.forEach { event ->
                EventCardPreview(event = event, modifier = Modifier.padding(vertical = 8.dp))
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
    val sampleEvents = listOf(
        Event(1, "Concert Night", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0, 0,"h", 0,true),
        Event(2, "Crafternoon", "Lots of fun arts and crafts", "Downtown Theater",listOf("NAMI"), 0,"2025-05-01", "6:30 PM", 0,"8:00 PM", "8:00 PM", listOf("art, fun"), 0,0,"h", 0),
        Event(3, "Concert Night2", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h", 0, true),
        Event(4, "Concert Night3", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h",0, true),
        Event(5, "Concert Night4", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h",0, true),
        Event(6, "Concert Night5", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h", 0, true),
        Event(7, "Concert Night6", "Live music and fun!","Downtown Theater", listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h", 0, true),
        Event(8, "Concert Night7", "Live music and fun!","Downtown Theater", listOf("Music Club"), 0,"2025-04-20", "8:00 PM",  0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h",0, true),
    )

    FavoritesScreen(events = sampleEvents)
}