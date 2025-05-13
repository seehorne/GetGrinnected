package screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.GetGrinnected.myapplication.AppRepository
import com.GetGrinnected.myapplication.Event
import com.GetGrinnected.myapplication.R
import com.GetGrinnected.myapplication.toEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * A composable function that represents the search screen of our app.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchScreen() {
    // Gets events from our repo
    val eventEntities by AppRepository.events
    // Converts events to event data type
    val events = eventEntities.map { it.toEvent() }
    // String associated with storing and changing the username of an account when we edit
    var description by remember { mutableStateOf("") }
    // state to say are we searching now
    val search = remember { mutableStateOf(false) }
    // holds the the date if it has been selected
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize())
    {
        //creates a top box for search page
        Box(
            modifier = Modifier
                .background(color = colorScheme.primary)
                .fillMaxWidth()
                .height(100.dp),
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
            ){
                // adds the logo to the top
                Image(
                    painter = painterResource(id = R.drawable.getgrinnected_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .background(Color.White)
                        .size(50.dp)
                )
                // the actual search bar
                OutlinedTextField(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            brush = SolidColor(colorScheme.onPrimary),
                            shape = CircleShape,),
                    shape = CircleShape,
                    value = description,
                    onValueChange = {
                        search.value = false
                        description = it
                    },
                    textStyle = TextStyle(color = colorScheme.onPrimary),
                    singleLine = true,
                    // creates the label for the search bar
                    label = {
                        Row {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search events",
                                tint = colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp))
                            androidx.compose.material3.Text(
                                "Search Events",
                                color = colorScheme.onPrimary,
                                style = typography.labelLarge
                            )
                        }
                    }
                )
                selectedDate = datePickerDocked()
            }
        }
        // creates the search button
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center) { // creates day menu
            Button(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp),
                onClick = { search.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search events",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp))
                    Text("Search" ,
                        color = colorScheme.onPrimary,
                        style = typography.labelLarge)
                }
            }
        }
        // actually sorts events based on search's
        if (search.value) {
            val sortedEvents = mutableListOf<Event>()
            if(description != ""){
                for (e in events.indices){
                    if (selectedDate != ""){
                        if (events[e].event_name.uppercase().contains(description.uppercase())
                            && events[e].event_start_time.contains(selectedDate)) {
                            sortedEvents.add(events[e])
                        }
                        if (events[e].event_description.uppercase().contains(description.uppercase())
                            && events[e].event_start_time.contains(selectedDate)) {
                            sortedEvents.add(events[e])
                        }
                        if ((events[e].organizations?.toString()?.uppercase()
                            ?.contains(description.uppercase()) != false)
                            && events[e].event_start_time.contains(selectedDate)
                        ) {
                            sortedEvents.add(events[e])
                        }
                        if (events[e].event_location?.uppercase()
                                ?.contains(description.uppercase()) != false
                            && events[e].event_start_time.contains(selectedDate)) {
                            sortedEvents.add(events[e])
                        }
                    }
                    else {
                        if (events[e].event_name.uppercase().contains(description.uppercase())) {
                            sortedEvents.add(events[e])
                        }
                        if (events[e].event_description.uppercase()
                                .contains(description.uppercase())
                        ) {
                            sortedEvents.add(events[e])
                        }
                        if (events[e].organizations?.toString()?.uppercase()
                                ?.contains(description.uppercase()) != false
                        ) {
                            sortedEvents.add(events[e])
                        }
                        if (events[e].event_location?.uppercase()
                                ?.contains(description.uppercase()) != false
                        ) {
                            sortedEvents.add(events[e])
                        }
                    }
                }
            } else{
                if (selectedDate != ""){
                    for (e in events.indices) {
                        if (events[e].event_start_time.contains(selectedDate)){
                            sortedEvents.add(events[e])
                        }
                    }
                }
            }
            SearchResults(sortedEvents.distinct())
        }
    }
}

/**
 * Creates a date picking composable as designed Android developer.com
 * It creates a calendar dropdown that makes selecting any day easy.
 * Currently doesn't work as it is off by a day and its return value is not the correct date format
 * @return a String date that was selected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePickerDocked(): String{
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    val timeZone: TimeZone = TimeZone.getDefault()
    val formatter = SimpleDateFormat(
        selectedDate, Locale.getDefault()
    )
    formatter.timeZone = timeZone
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { showDatePicker = !showDatePicker }) {
            Icon(
                imageVector = Icons.Default.DateRange,
                tint = colorScheme.onPrimary,
                contentDescription = "Select date"
            )
        }
        // shows the dates to be picked.
        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
    if (selectedDate != ""){
        val update = selectedDate.substring(9).toInt() + 1
        return selectedDate.substring(0, 9) + update.toString()
    }
    else{
        return selectedDate
    }
}

/**
 * takes a millis and returns a date
 */
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}