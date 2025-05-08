package screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.Event
import com.example.myapplication.EventCard

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchResults(events: List<Event>, modifier: Modifier = Modifier){
    // remembers where we are scrolled to
    val state = rememberScrollState()
    // makes the page scrollable
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    Column(
        modifier = modifier.fillMaxSize()
        .verticalScroll(state),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        for(e in events.indices){
            EventCard(events[e])
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}