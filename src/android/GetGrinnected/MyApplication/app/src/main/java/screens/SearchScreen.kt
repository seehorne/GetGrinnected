package screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.GetGrinnected.myapplication.AppRepository
import com.GetGrinnected.myapplication.Event
import com.GetGrinnected.myapplication.R
import com.GetGrinnected.myapplication.toEvent
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

/**
 * A composable function that represents the search screen of our app.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchScreen() {
    // grabs the devices current date
    val today = LocalDate.now()
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
        Box(
            modifier = Modifier
                .background(color = colorScheme.primary)
                .fillMaxWidth()
                .height(100.dp),
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 25.dp, end = 6.dp)
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
                androidx.compose.material3.OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.secondary),
                    value = description,
                    onValueChange = {
                        search.value = false
                        description = it
                    },
                    singleLine = true,
                    label = {
                        Row(modifier = Modifier) {
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
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // creates day menu
            selectedDate = datePickerDocked()
        }
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) { // creates day menu
            Button(
                onClick = { search.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Search")
                }
            }
        }
        if (search.value) {
            var sortedEvents = mutableListOf<Event>()
            if(description != ""){
                for (e in events.indices){
                    if (selectedDate != ""){
                        if (events[e].event_name.uppercase().contains(description.uppercase())
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        } else if (events[e].event_description.uppercase().contains(description.uppercase())
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        } else if ((events[e].organizations?.toString()?.uppercase()
                                ?.contains(description.uppercase()) ?: true) == true
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        }else if (events[e].event_location?.uppercase()
                                ?.contains(description.uppercase()) ?: true
                            && events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))) {
                            sortedEvents.add(events[e])
                        }
                    }
                    else {
                        if (events[e].event_name.uppercase().contains(description.uppercase())) {
                            sortedEvents.add(events[e])
                        } else if (events[e].event_description.uppercase()
                                .contains(description.uppercase())
                        ) {
                            sortedEvents.add(events[e])
                        } else if (events[e].organizations?.toString()?.uppercase()
                                ?.contains(description.uppercase()) != false
                        ) {
                            sortedEvents.add(events[e])
                        }else if (events[e].event_location?.uppercase()
                                ?.contains(description.uppercase()) != false
                        ) {
                            sortedEvents.add(events[e])
                        }
                    }
                }
            } else{
                if (selectedDate != ""){
                    for (e in events.indices) {
                        if (events[e].event_start_time.contains(selectedDate.substring(6..9) + "-" + selectedDate.substring(0..1) + "-" + selectedDate.substring(3..4))){
                            sortedEvents.add(events[e])
                        }
                    }
                } else {
                    sortedEvents = events.toMutableList()
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

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Event Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

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
    return selectedDate
}

/**
 * takes a millis and returns a date
 */
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}