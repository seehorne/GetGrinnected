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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.AppRepository
import com.example.myapplication.Check
import com.example.myapplication.CheckBox
import com.example.myapplication.Event
import com.example.myapplication.EventCard
import com.example.myapplication.R
import com.example.myapplication.toEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * A composable function that represents the Calendar screen of our app. (More to come)
 *
 * @param modifier Modifier to be applied to the screen layout.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(tags: List<Check>, modifier: Modifier = Modifier) {
    // Gets events from our repo
    val eventEntities by AppRepository.events
    // Converts them to event data type
    val event = eventEntities.map { it.toEvent() }
    // Sorts them by time
    event.sortedBy { it.event_time }
    val date = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MM")
    val currentMonth = date.format(formatter).toString()
    val chosenTags = mutableListOf<String>()
    var selectedView by remember { mutableIntStateOf(2) }
    // tracks dropdowns based on buttons
    val expanded = remember { mutableStateOf(false) }
    val expanded2 = remember { mutableStateOf(false) }
    var calendarInputList by remember {
        mutableStateOf(createCalendarList(event, chosenTags, currentMonth))
    }
    var clickedCalendarElem by remember {
        mutableStateOf<CalendarInput?>(null)
    }
    val scrollState = rememberScrollState()
    // background color for the page
    val gradient =
        Brush.verticalGradient(
            listOf(Color.Red, Color.Blue, Color.Green),
            0.0f,
            10000.0f,
            TileMode.Repeated
        )
    // sets the month based on devices local date
    val month = (when (currentMonth) {
        "01" -> { "January" }
        "02" -> { "February" }
        "03" -> { "March" }
        "04" -> { "April" }
        "05" -> { "May" }
        "06" -> { "June" }
        "07" -> { "July" }
        "08" -> { "August" }
        "09" -> { "September" }
        "10" -> { "October" }
        "11" -> { "November" }
        else -> {
            "December"
        }
    }).toString()
    Column(modifier = modifier.fillMaxSize().background(gradient)) {
        Box(modifier = modifier.background(Color.Black).size(width = 450.dp, height = 100.dp)) {
            Row{
                // adds the logo to the top
                Image(
                    painter = painterResource(id = R.drawable.gg_logo_2),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .padding(25.dp)
                        .background(Color.White)
                        .size(50.dp)
                )
                // centers the bottons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    // I don't know why this is necessary but was needed to properly space tag menu
                    Row(
                        modifier = Modifier
                            .padding(25.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        // creates tags menu
                        Button(onClick = { expanded2.value = true }) {
                            Text("Tags")
                        }
                        DropdownMenu(
                            expanded = expanded2.value,
                            onDismissRequest = { expanded2.value = false
                                for (i in tags.indices){
                                    if (tags[i].checked) {
                                        chosenTags.add(tags[i].label)
                                    }
                                }
                                calendarInputList = createCalendarList(event, chosenTags, currentMonth) }) {
                            for (i in tags.indices) {
                                DropdownMenuItem(
                                    text = {
                                        CheckBox(
                                            check = tags[i]
                                        )
                                    },
                                    onClick = {}
                                )
                            }
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                        Spacer(modifier = Modifier.width(30.dp))
                    Button(onClick = { expanded.value = true }) {
                        when (selectedView) {
                            0 -> { Text("Day") }
                            1 -> { Text("Week") }
                            else -> { Text("Month") }
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
                }
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
                    month = month
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
                            text = month + " (Day ${dayInfo.day})",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // sorts by tags
                        for (i in tags.indices){
                            if (tags[i].checked) {
                                chosenTags.add(tags[i].label)
                            }
                        }
                        calendarInputList = createCalendarList(event, chosenTags, currentMonth)
                        // populates events based on selected days
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
@RequiresApi(Build.VERSION_CODES.O)
fun createCalendarList(event: List<Event>, tags: List<String>, month: String): List<CalendarInput>{
    val calendarInputs = mutableListOf<CalendarInput>()
    val currentDay = mutableListOf<Event>()
    // sorts events
    for(i in 1 .. 31){
        for(j in event.indices){
            if (tags.isEmpty()) {
                if (event[j].event_start_time.substring(8, 10) == i.toString()
                    && event[j].event_start_time.substring(5, 7) == month
                ) {
                    currentDay.add(event[j])
                }
            }
            else {
                for(t in tags.indices){
                if(event[j].event_start_time.substring(8, 10) == i.toString()
                    && event[j].event_start_time.substring(5, 7) == month
                    && event[j].tags.contains(tags[t]))
                     {
                        currentDay.add(event[j])
                        break
                    }
                }
            }

        }
        calendarInputs.add(
            CalendarInput(
                i,
                events = currentDay.toMutableList()
            )
            )
        currentDay.clear()
    }
    return calendarInputs
}
