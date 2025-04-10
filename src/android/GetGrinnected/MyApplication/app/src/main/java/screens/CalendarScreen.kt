package screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Event
import com.example.myapplication.EventCard

/**
 * A composable function that represents the Calendar screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 */
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    var selectedView by remember { mutableIntStateOf(2) }
    val expanded = remember { mutableStateOf(false) }
    val calendarInputList by remember {
        mutableStateOf(createCalendarList())
    }
    var clickedCalendarElem by remember {
        mutableStateOf<CalendarInput?>(null)
    }
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = modifier.padding(16.dp)) {
            Row {
                Button(onClick = { expanded.value = true }) {
                    if (selectedView == 0) {
                        Text("Day")
                    } else if (selectedView == 1) {
                        Text("Week")
                    } else {
                        Text("Month")
                    }
                }
            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(text = { Text("Day View") }, onClick = {
                    selectedView = 0
                    expanded.value = false
                })
                DropdownMenuItem(text = { Text("Week View") }, onClick = {
                    selectedView = 1
                    expanded.value = false
                })
                DropdownMenuItem(text = { Text("Month View") }, onClick = {
                    selectedView = 2
                    expanded.value = false
                })
            }
        }
        when (selectedView) {
            0 -> DayViewScreen(modifier)
            1 -> WeekViewScreen(modifier)
            2 -> {
                MonthViewScreen(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .aspectRatio(1.3f),
                    calendarInput = calendarInputList,
                    onDayClick = { day ->
                        clickedCalendarElem = calendarInputList.first { it.day == day }
                    },
                    month = "April"
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    clickedCalendarElem?.let { dayInfo ->
                        Text(
                            text = "April (Day ${dayInfo.day})",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        dayInfo.events.forEach {
                            EventCard(it)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Creates the hard coded calendar input list for use in the calendar
 * @return a list of CalendarInput values
 */
private fun createCalendarList(): List<CalendarInput>{
    val calendarInputs = mutableListOf<CalendarInput>()
    for(i in 1 .. 31){
        calendarInputs.add(
            CalendarInput(
                i,
                events = listOf(
                    Event(eventid= 2, event_name = "Crafternoon", event_description =  "Lots of fun arts and crafts", event_location = "Downtown Theater", organizations = listOf("NAMI"), rsvp = 0, event_date ="2025-05-01", event_start_time = "6:30 PM", event_private = 0, event_end_time = "8:00 PM", event_time ="8:00 PM", tags =listOf("art, fun"), is_draft = 0,repeats = 0, event_image = "h", event_all_day = 0, is_favorited = true)
                )
            )
        )
    }
    return calendarInputs
}

/**
 * Preview used specifically for UI design purposes
 */
@Preview (showBackground = true)
@Composable
fun CalendarScreenPreview (modifier: Modifier = Modifier){
    CalendarScreen(modifier)
}